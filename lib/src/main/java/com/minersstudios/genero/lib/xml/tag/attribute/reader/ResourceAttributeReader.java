package com.minersstudios.genero.lib.xml.tag.attribute.reader;

import android.content.res.XmlResourceParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.minersstudios.genero.lib.xml.tag.attribute.ResourceAttribute;

import org.jetbrains.annotations.Contract;

public interface ResourceAttributeReader extends Reader<ResourceAttribute> {

    /**
     * Returns the value of the "id" attribute or null if there is not one
     *
     * @return The value of the "id" attribute or null
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @Nullable String getIdAttribute() throws IllegalStateException;

    /**
     * Returns the value of the "class" attribute or null if there is not one
     *
     * @return The value of the "class" attribute or null
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @Nullable String getClassAttribute() throws IllegalStateException;

    /**
     * Returns the value of the "style" attribute or 0 if there is not one
     *
     * @return The value of the "style" attribute or 0
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getStyleAttribute() throws IllegalStateException;

    /**
     * Returns the integer value of the "id" attribute or defaultValue if there
     * is none
     *
     * @param defaultValue What to return if the "id" attribute isn't found
     * @return The integer value of the "id" attribute or defaultValue
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getIdAttributeResourceValue(final int defaultValue) throws IllegalStateException;

    /**
     * Creates a new instance of {@link ResourceAttributeReader} with the given
     * parser
     *
     * @param parser The parser to read from
     * @return A new instance of {@code ResourceAttributeReader}
     */
    @Contract("_ -> new")
    static @NonNull ResourceAttributeReader create(final @NonNull XmlResourceParser parser) {
        return new ResourceAttributeReaderImpl(parser);
    }
}
