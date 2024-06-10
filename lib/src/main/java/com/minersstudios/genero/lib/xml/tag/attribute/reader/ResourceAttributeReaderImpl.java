package com.minersstudios.genero.lib.xml.tag.attribute.reader;

import android.content.res.XmlResourceParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.minersstudios.genero.lib.xml.tag.attribute.ResourceAttribute;

class ResourceAttributeReaderImpl extends ReaderImpl<ResourceAttribute, XmlResourceParser> implements ResourceAttributeReader {

    ResourceAttributeReaderImpl(final @NonNull XmlResourceParser parser) {
        super(parser);
    }

    @Override
    public @NonNull ResourceAttribute getAttribute(final int index) throws IndexOutOfBoundsException, IllegalStateException {
        return ResourceAttribute.create(this.getParser(), index);
    }

    @Override
    public @Nullable String getIdAttribute() throws IllegalStateException {
        return this.getParser().getIdAttribute();
    }

    @Override
    public @Nullable String getClassAttribute() throws IllegalStateException {
        return this.getParser().getClassAttribute();
    }

    @Override
    public int getStyleAttribute() throws IllegalStateException {
        return this.getParser().getStyleAttribute();
    }

    @Override
    public int getIdAttributeResourceValue(final int defaultValue) throws IllegalStateException {
        return this.getParser().getIdAttributeResourceValue(defaultValue);
    }
}
