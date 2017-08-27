/*
 * Copyright (c) 2017 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * jtsgen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jtsgen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jtsgen.  If not, see http://www.gnu.org/licenses/
 *
 */

package dz.jtsgen.processor.dsl.parser;

import dz.jtsgen.processor.dsl.CustomMappingParser;
import dz.jtsgen.processor.dsl.model.*;
import dz.jtsgen.processor.model.ConversionCoverage;
import dz.jtsgen.processor.util.Either;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.dsl.parser.ParserState.*;
import static dz.jtsgen.processor.dsl.parser.TokenType.INVALID;
import static dz.jtsgen.processor.dsl.parser.TokenType.JIDENT;

/**
 * Hand written parser for custom type mapping expressions
 */
public class CustomMappingParserImpl implements CustomMappingParser {

    private static Logger LOG = Logger.getLogger(CustomMappingParserImpl.class.getName());

    private final Lexer lexer;

    private ParserState pState = JAVA_NAMES;

    private final List<String> javaTypevars = new LinkedList<>();
    private final List<String> javaNames = new LinkedList<>();
    private final List<TSExpressionElement> tsLiterals = new LinkedList<>();

    private String errorMsg = "";

    private ConversionCoverage arrow = null;
    private Token lastToken = TokenBuilder.of(INVALID,"", -1);

    CustomMappingParserImpl(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public Either<String, TypeMappingExpression> parse(String expression) {
        final List<Token> lexed = lexer.lex(expression);
        LOG.finest( () -> "CMP tokens of '" + expression + "' = " + lexed);
        label:
        for (Token token : lexed) {
            LOG.finest( () -> "CMP ( " + pState.ordinal() + "): token=" + token);
            if (token.type() == INVALID)
                return Either.left("Invalid token '" + token.data() + "' at pos " + token.index() + " of expression " + expression);
            switch (token.type())  {
                case WHITESPACE: continue label;
                case JIDENT: processIdent(token); break;
                case DOT: processDot(token); break;
                case ARROW: processArrow(token); break;
                case DARROW: processArrow(token); break;
                case ANGLE_OPEN: processAngleOpen(token); break ;
                case ANGLE_CLOSE: processAngleClose(token); break ;
                case DELIM: processDelim(token); break;
                case BACKTICK: processBacktick(token); break;
                case TSLIT: processTsLit(token); break;
                default: errorMsg="Unhandled Token " + token;
            }
            if (!errorMsg.isEmpty()) return Either.left(errorMsg +"'" + token.data() + "' at pos " + token.index() + " of expression " + expression);
            this.lastToken = token;
        }

        return Either.right(
                TypeMappingExpressionBuilder.builder()
                        .addAllNames(this.javaNames)
                        .addAllTypeVariables(this.javaTypevars)
                        .conversionCoverage(this.arrow)
                        .addAllTsExpressionElements(this.tsLiterals)
                .build()
        );
    }

    private void processBacktick(Token token) {
        if (pState==TS_PARSING) pState=TS_SINGLE_TYPE;
        else if (pState==TS_CLOSING_SINGLE_TYPE) pState=TS_PARSING;
        else this.errorMsg="backtick only allowed RHS for Type Variables";
    }

    private void processAngleClose(Token token) {
        if (pState == JAVA_TYPES) this.pState = JAVA_NAMES;
        else if (pState == TS_TYPES) {
            addTsLit(token);
            this.pState = TS_PARSING;
        }
        else errorMsg = "closing angle bracket without opening";

    }

    private void processDelim(Token token) {
        if (pState == TS_TYPES) addTsLit(token);
        else if (pState != JAVA_TYPES) this.errorMsg="unexpected separator";
    }

    private void processAngleOpen(Token token) {
        if (this.pState==JAVA_NAMES) this.pState = JAVA_TYPES;
        else if (this.pState==TS_PARSING) {
            addTsLit(token);
            this.pState = TS_TYPES;
        }
        else this.errorMsg="unexpected bracket while " + pState;
    }


    private void processTsLit(Token token) {
        if (this.pState==TS_PARSING) tsLiterals.add(TSMappedTerminalBuilder.of( token.data()) );
        else if (this.pState==TS_TYPES ) addTsVar(token);
        else if (this.pState==TS_SINGLE_TYPE) {
            addTsVar(token);
            this.pState=TS_CLOSING_SINGLE_TYPE;
        }
        else this.errorMsg="unexpeced Type Script Identifier";
    }

    private void addTsLit(Token token) {
        tsLiterals.add(TSMappedTerminalBuilder.of( token.data()) );
    }

    private void addTsVar(Token token) {
        if (this.javaTypevars.stream().anyMatch(x -> x.equals(token.data())))
          tsLiterals.add(TSMappedTypeVarBuilder.of( token.data()) );
        else this.errorMsg="TS type variable not defined on LHS (" + this.javaTypevars.stream().collect(Collectors.joining()) + ")";
    }

    private void processArrow(Token token) {
        if (this.pState==JAVA_NAMES) {
            Optional<ConversionCoverage> firstArrow = (Arrays.stream(ConversionCoverage.values())).filter(x -> x.arrowLiteral().equals(token.data())).findFirst();
            firstArrow.ifPresent(x -> this.arrow = x);
            this.pState = TS_PARSING;
        }
    }

    private void processDot(Token token) {
        if (pState==JAVA_NAMES)
            if (lastToken.type()!=INVALID && lastToken.type()!=JIDENT) this.errorMsg="expecting dot separating names but got ";
        else if (pState==JAVA_TYPES) this.errorMsg="only type variables allowed between brackets";
    }

    private void processIdent(Token token) {
        if (pState==JAVA_NAMES) this.javaNames.add(token.data());
        else if (pState==JAVA_TYPES) this.javaTypevars.add(token.data());
        else this.errorMsg="Unexpexted identifier ";


    }
}
