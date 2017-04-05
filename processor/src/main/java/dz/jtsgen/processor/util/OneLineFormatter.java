package dz.jtsgen.processor.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class OneLineFormatter extends Formatter {
    private static final String LF = System.getProperty("line.separator");
    private final long FIRST_TIME = System.currentTimeMillis();

    @Override
    public String format(LogRecord x) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%6d",System.currentTimeMillis() - FIRST_TIME) )
                .append(" ")
                .append(String.format("%-7s",x.getLevel().getName()))
                .append(formatMessage(x));

        if (x.getThrown() != null) {
            try {
                sb.append(LF).append("exception: ");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                x.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
                sb.append("while retrieving exception: ");
                sb.append(ex.getMessage());
                sb.append(" ");
            }
        }

        sb.append(LF);
        return sb.toString();
    }
}
