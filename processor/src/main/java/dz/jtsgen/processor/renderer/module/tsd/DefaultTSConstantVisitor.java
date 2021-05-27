package dz.jtsgen.processor.renderer.module.tsd;

import dz.jtsgen.processor.helper.IdentHelper;
import dz.jtsgen.processor.model.TSConstant;
import dz.jtsgen.processor.model.rendering.TSConstantVisitor;

import java.io.PrintWriter;

public final class DefaultTSConstantVisitor extends OutputVisitor implements TSConstantVisitor {

    DefaultTSConstantVisitor(PrintWriter out, boolean printFullName) {
        super(out, printFullName);
    }

    @Override
    public void visit(TSConstant x, int ident) {
        x.getComment().ifPresent( comment -> tsComment(comment, ident));

        Object constantValue = x.getConstantValue();

        if(constantValue instanceof String) {
            constantValue = "\"" + constantValue + "\"";
        }

        PrintWriter out = getOut();

        out.print(IdentHelper.identPrefix(ident));
        out.print("export const ");
        out.print(x.getName());
        out.print(":");
        out.print(x.getType());
        out.print(" = ");
        out.print(constantValue);
        out.println(";");

    }
}
