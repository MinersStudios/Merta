package com.minersstudios.genero.lib.xml.tag.attribute;

import static com.minersstudios.genero.lib.xml.tag.attribute.ResourceAttributeImpl.NOT_SET;

import android.content.res.XmlResourceParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

public interface ResourceAttribute extends Attribute {

    /**
     * Returns the resource ID of the current attribute.
     * <p>
     * This will be the identifier for an attribute resource, which can be used
     * by styles. Returns 0 if there is no resource associated with this
     * attribute.
     *
     * @return The resource identifier, 0 if none
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getNameResource() throws IllegalStateException;

    /**
     * Return the index of the value of attribute at its index in the list
     * 'options'
     *
     * @param options      List of strings whose values we are checking against
     * @param defaultValue Value returned if attribute doesn't exist or no match
     *                     is found
     * @return Index in to 'options' or defaultValue
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getListValue(
            final int defaultValue,
            final @NonNull String... options
    ) throws IllegalStateException;

    /**
     * Return the boolean value of attribute
     *
     * @param defaultValue Default value to return
     * @return The boolean value of the attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    boolean getBooleanValue(final boolean defaultValue) throws IllegalStateException;

    /**
     * Return the value of attribute as a resource identifier
     *
     * @param defaultValue Default value to return
     * @return The resource identifier of the attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getResourceValue(final int defaultValue) throws IllegalStateException;

    /**
     * Return the integer value of attribute
     *
     * @param defaultValue Default value to return
     * @return The integer value of the attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getIntValue(final int defaultValue) throws IllegalStateException;

    /**
     * Return the integer value of attribute that is formatted as an unsigned
     * value.
     * <p>
     * In particular, the formats 0xn...n and #n...n are handled.
     *
     * @param defaultValue Default value to return
     * @return The unsigned integer value of the attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getUnsignedIntValue(final int defaultValue) throws IllegalStateException;

    /**
     * Return the float value of attribute
     *
     * @param defaultValue Default value to return
     * @return The float value of the attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    float getFloatValue(final float defaultValue) throws IllegalStateException;

    /**
     * Creates a new instance of attribute with the given parser and index
     *
     * @param parser The parser to use
     * @param index  The index of the attribute
     * @return A new instance of {@link ResourceAttribute}
     * @throws IndexOutOfBoundsException If the index is out of bounds for the
     *                                   attribute count in the parser
     */
    @Contract("_, _ -> new")
    static @NonNull ResourceAttribute create(
            final @NonNull XmlResourceParser parser,
            final int index
    ) throws IndexOutOfBoundsException {
        return new ResourceAttributeImpl(parser, index);
    }

    /**
     * Creates a new instance of attribute with the given values
     *
     * @param index            The index of the attribute
     * @param namespace        The namespace of the attribute
     * @param name             The name of the attribute
     * @param prefix           The prefix of the attribute
     * @param type             The type of the attribute
     * @param value            The value of the attribute
     * @param nameResource     The resource ID of the attribute name
     * @param listValue        The index of the value of attribute at its index in the list
     *                         'options'
     * @param booleanValue     The boolean value of the attribute
     * @param resourceValue    The value of attribute as a resource identifier
     * @param intValue         The integer value of the attribute
     * @param unsignedIntValue The integer value of attribute that is formatted as an unsigned
     *                         value
     * @param floatValue        The float value of the attribute
     * @param isDefault        Whether the attribute is default
     * @return A new instance of {@link ResourceAttribute}
     */
    @Contract("_, _, _, _, _, _, _, _, _, _, _, _, _, _ -> new")
    static @NonNull ResourceAttribute create(
            final int index,
            final @NonNull String namespace,
            final @NonNull String name,
            final @Nullable String prefix,
            final @NonNull String type,
            final @NonNull String value,
            final int nameResource,
            final int listValue,
            final @Nullable Boolean booleanValue,
            final int resourceValue,
            final int intValue,
            final int unsignedIntValue,
            final float floatValue,
            final boolean isDefault
    ) {
        return new ResourceAttribute() {

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
            public int getNameResource() {
                return nameResource == NOT_SET ? 0 : nameResource;
            }

            @Override
            public int getListValue(
                    final int defaultValue,
                    @NonNull final String... options
            ) {
                return listValue == NOT_SET ? defaultValue : listValue;
            }

            @Override
            public boolean getBooleanValue(final boolean defaultValue) {
                return booleanValue == null ? defaultValue : booleanValue;
            }

            @Override
            public int getResourceValue(final int defaultValue) {
                return resourceValue == NOT_SET ? defaultValue : resourceValue;
            }

            @Override
            public int getIntValue(final int defaultValue) {
                return intValue == NOT_SET ? defaultValue : intValue;
            }

            @Override
            public int getUnsignedIntValue(final int defaultValue) {
                return unsignedIntValue == NOT_SET ? defaultValue : unsignedIntValue;
            }

            @Override
            public float getFloatValue(final float defaultValue) {
                return floatValue == NOT_SET ? defaultValue : floatValue;
            }

            @Override
            public boolean isDefault() {
                return isDefault;
            }
        };
    }
}
