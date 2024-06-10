package com.minersstudios.genero.lib.xml.tag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

public abstract class ElementTag extends XmlTagImpl {
    private String namespace;
    private String name;
    private String prefix;

    ElementTag(final @NonNull XmlPullParser parser) {
        super(parser);
    }

    /**
     * Returns the namespace URI of the current element.
     * <p>
     * The default namespace is represented as empty string.
     *
     * @return The namespace URI of the current element
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public @NonNull String getNamespace() throws IllegalStateException {
        if (this.namespace == null) {
            this.namespace = this.getParser().getNamespace();
        }

        return this.namespace;
    }

    /**
     * Returns the name of the current element.
     * <p>
     * When namespace processing is disabled, the raw name is returned.
     *
     * @return The name of the current element
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public @NonNull String getName() throws IllegalStateException {
        if (this.name == null) {
            this.name = this.getParser().getName();
        }

        return this.name;
    }

    /**
     * Returns the prefix of the current element.
     * <p>
     * If the element is in the default namespace (has no prefix), null is
     * returned.
     *
     * @return The prefix of the current element or null
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public @Nullable String getPrefix() throws IllegalStateException {
        if (this.prefix == null) {
            this.prefix = this.getParser().getPrefix();
        }

        return this.prefix;
    }
}
