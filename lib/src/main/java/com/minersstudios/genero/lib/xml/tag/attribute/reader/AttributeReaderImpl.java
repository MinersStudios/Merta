package com.minersstudios.genero.lib.xml.tag.attribute.reader;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.xml.tag.attribute.Attribute;

import org.xmlpull.v1.XmlPullParser;

class AttributeReaderImpl extends ReaderImpl<Attribute, XmlPullParser> implements AttributeReader {

    AttributeReaderImpl(final @NonNull XmlPullParser parser) {
        super(parser);
    }

    @Override
    public @NonNull Attribute getAttribute(final int index) throws IndexOutOfBoundsException, IllegalStateException {
        return Attribute.create(this.getParser(), index);
    }
}
