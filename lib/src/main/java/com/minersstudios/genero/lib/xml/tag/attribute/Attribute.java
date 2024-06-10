package com.minersstudios.genero.lib.xml.tag.attribute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

public interface Attribute {

    /**
     * Returns the index of the current attribute in the element
     *
     * @return The index of the current attribute in the element
     */
    int getIndex();

    /**
     * Returns the namespace URI of the current attribute.
     * <p>
     * Returns an empty string ("") if namespaces are not enabled or the
     * attribute has no namespace.
     *
     * @return The namespace URI of the current attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @NonNull String getNamespace() throws IllegalStateException;

    /**
     * Returns the local name of the attribute if namespaces are enabled or just
     * attribute name if namespaces are disabled.
     *
     * @return The name of the current attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @NonNull String getName() throws IllegalStateException;

    /**
     * Returns the prefix of the current attribute.
     * <p>
     * Returns null if the element has no prefix or if namespaces are disabled.
     *
     * @return The prefix of the current attribute or null
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @Nullable String getPrefix() throws IllegalStateException;

    /**
     * Returns the type of the current attribute.
     * <p>
     * If parser is non-validating it MUST return CDATA.
     *
     * @return The type of the current attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @NonNull String getType() throws IllegalStateException;

    /**
     * Returns the value of the current attribute.
     * <p>
     * <b>NOTE:</b> The value must be normalized (including entity replacement
     *              text if PROCESS_DOCDECL is false) as described in
     *              <a href="http://www.w3.org/TR/REC-xml#AVNormalize">XML 1.0
     *              section 3.3.3 Attribute-Value Normalization</a>
     *
     * @return The value of the current attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @NonNull String getValue() throws IllegalStateException;

    /**
     * Returns if the current attribute was not in input was declared in XML.
     * <p>
     * If parser is non-validating it MUST always return false.
     *
     * @return False if attribute was in input
     * @throws IllegalStateException If the parser is in an invalid state
     */
    boolean isDefault() throws IllegalStateException;

    /**
     * Creates a new instance of attribute with the given parser and index
     *
     * @param parser The parser to use
     * @param index  The index of the attribute
     * @return A new instance of {@link Attribute}
     * @throws IndexOutOfBoundsException If the index is out of bounds for the
     *                                   attribute count in the parser
     */
    @Contract("_, _ -> new")
    static @NonNull Attribute create(
            final @NonNull XmlPullParser parser,
            final int index
    ) throws IndexOutOfBoundsException {
        return new RawAttribute<>(parser, index);
    }

    /**
     * Creates a new instance of attribute with the given values
     *
     * @param index     The index of the attribute
     * @param namespace The namespace of the attribute
     * @param name      The name of the attribute
     * @param prefix    The prefix of the attribute
     * @param type      The type of the attribute
     * @param value     The value of the attribute
     * @param isDefault Whether the attribute is default
     * @return A new instance of {@link Attribute}
     */
    @Contract("_, _, _, _, _, _, _ -> new")
    static @NonNull Attribute create(
            final int index,
            final @NonNull String namespace,
            final @NonNull String name,
            final @Nullable String prefix,
            final @NonNull String type,
            final @NonNull String value,
            final boolean isDefault
    ) {
        return new Attribute() {

            @Override
            public int getIndex() {
                return index;
            }

            @Override
            public @NonNull String getNamespace() {
                return namespace;
            }

            @Override
            public @NonNull String getName() {
                return name;
            }

            @Override
            public @Nullable String getPrefix() {
                return prefix;
            }

            @Override
            public @NonNull String getType() {
                return type;
            }

            @Override
            public @NonNull String getValue() {
                return value;
            }

            @Override
            public boolean isDefault() {
                return isDefault;
            }
        };
    }
}
