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

package dz.jtsgen.processor.renderer.helper;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple PrintWriter Wrapper, that puts the content to the log at level fine.
 */
public class PrintWriterWithLogging extends PrintWriter {

    private static Logger LOG = Logger.getLogger(PrintWriterWithLogging.class.getName());

    private final StringBuilder logOutput = new StringBuilder();

    public PrintWriterWithLogging(Writer out, String name) {
        super(out);
        logOutput.append("output of file:");
        logOutput.append(name);
        logOutput.append(System.lineSeparator());
    }

    @Override
    public void close() {
        super.close();
        if (LOG.isLoggable(Level.FINE)) LOG.fine(logOutput.toString());
    }


    @Override
    public void println() {
        super.println();
        if (LOG.isLoggable(Level.FINE)) logOutput.append(System.lineSeparator());
    }

    @Override
    public void write(int c) {
        super.write(c);
        if (LOG.isLoggable(Level.FINE)) this.logOutput.append(c);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        super.write(buf, off, len);
        if (LOG.isLoggable(Level.FINE)) this.logOutput.append(buf, off, len);
    }


    @Override
    public void write(String s, int off, int len) {
        super.write(s, off, len);
        if (LOG.isLoggable(Level.FINE)) this.logOutput.append(s, off, len);
    }

}
