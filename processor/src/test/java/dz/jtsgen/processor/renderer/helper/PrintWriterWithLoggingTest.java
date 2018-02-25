/*
 * Copyright (c) 2018 Dragan Zuvic
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PrintWriterWithLoggingTest {

    private Writer mockedWriter = mock(Writer.class);
    private PrintWriterWithLogging testee = new PrintWriterWithLogging(mockedWriter,"none");

    @BeforeEach
    void init() {
        this.mockedWriter=mock(Writer.class);
        this.testee = new PrintWriterWithLogging(mockedWriter,"none");
    }

    @Test
    @DisplayName("check printlogging calls superclass")
    void writeInt() throws IOException {
        testee.write(10);
        verify(mockedWriter, times(1)).write(10);
    }

    @Test
    @DisplayName("check printlogging calls superclass for char[]")
    void writeChar() throws IOException {
        testee.write(new char[]{'a'},0,1);
        verify(mockedWriter, times(1)).write(new char[]{'a'},0,1);
    }

    @Test
    void checkloglevel() {
        Logger logger = Logger.getLogger(PrintWriterWithLogging.class.getName());
        Level oldLevel = logger.getLevel();

        try {
            logger.setLevel(Level.FINE);
            testee.write(new char[]{'a'},0,1);
            testee.write(10);
            testee.println();
            testee.write("s",0,1);
            testee.close();
            // not interested in log output currently.
        } catch (Exception e) {
            fail("should not fail while checking log level logging");
        }
        finally {
            logger.setLevel(oldLevel);
        }
    }
}