/*
 * Copyright 2017 Dragan Zuvic
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
package dz.jtsgen.processor;


import dz.jtsgen.processor.util.OneLineFormatter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * handles logging, nothing else
 */
public abstract class AbstractProcessorWithLogging extends AbstractProcessor {

    private static Logger LOG = Logger.getLogger(AbstractProcessorWithLogging.class.getName());

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        final Optional<String> jtsgenLogLevel = Optional.ofNullable(processingEnv.getOptions().get("jtsgenLogLevel"));
        String packageName = AbstractProcessorWithLogging.class.getPackage().getName();
        final Logger logger = Logger.getLogger(packageName);
        if (jtsgenLogLevel.isPresent()) {
            Level level = jtsgenLogLevel
                    .map(
                            (x) -> {
                                try {
                                    return Level.parse(x.trim().toUpperCase());
                                } catch (IllegalArgumentException ex) {
                                    return Level.OFF;
                                }
                            }
                    ).orElse(Level.INFO);
            Formatter oneLineFormatter = new OneLineFormatter();
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(level);
            consoleHandler.setFormatter(oneLineFormatter);
            logger.setUseParentHandlers(false);
            logger.setLevel(level);

//        consoleHandler.setFormatter(oneLineFormatter);
            logger.addHandler(consoleHandler);
            LOG.log(Level.FINER,() -> String.format("LogLevel %s = %s ", packageName, level.getName()));
        } else {
            logger.setLevel(Level.OFF);
        }
    }

}

