package de.sander.til;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class StringMetrics {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(StringMetrics.class.getSimpleName());

	Font font;
    FontRenderContext context;

    public StringMetrics(Graphics2D g2) {
        font = g2.getFont();
        context = g2.getFontRenderContext();
    }

    Rectangle2D getBounds(String message) {
        return font.getStringBounds(message, context);
    }

    double getWidth(String message) {
        Rectangle2D bounds = getBounds(message);
        return bounds.getWidth();
    }

    double getHeight(String message) {
        Rectangle2D bounds = getBounds(message);
        return bounds.getHeight();
    }
	
}