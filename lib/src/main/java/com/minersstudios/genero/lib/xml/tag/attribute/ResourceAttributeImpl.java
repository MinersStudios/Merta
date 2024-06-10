package com.minersstudios.genero.lib.xml.tag.attribute;

import android.content.res.XmlResourceParser;

import androidx.annotation.NonNull;

class ResourceAttributeImpl extends RawAttribute<XmlResourceParser> implements ResourceAttribute {
    private int nameResource;
    private int listValue;
    private Boolean booleanValue;
    private int resourceValue;
    private int intValue;
    private int unsignedIntValue;
    private float floatValue;

    static final int NOT_SET = -2;

    ResourceAttributeImpl(
            final @NonNull XmlResourceParser parser,
            final int index
    ) throws IndexOutOfBoundsException {
        super(parser, index);

        this.nameResource = NOT_SET;
        this.listValue = NOT_SET;
        this.resourceValue = NOT_SET;
        this.intValue = NOT_SET;
        this.unsignedIntValue = NOT_SET;
        this.floatValue = NOT_SET;
    }

    @Override
    public int getNameResource() throws IllegalStateException {
        if (this.nameResource == NOT_SET) {
            this.nameResource = this.getParser().getAttributeNameResource(this.getIndex());
        }

        return this.nameResource;
    }

    @Override
    public int getListValue(
            final int defaultValue,
            @NonNull final String... options
    ) throws IllegalStateException {
        if (this.listValue == NOT_SET) {
            this.listValue = this.getParser().getAttributeListValue(this.getIndex(), options, defaultValue);
        }

        return this.listValue;
    }

    @Override
    public boolean getBooleanValue(final boolean defaultValue) throws IllegalStateException {
        if (this.booleanValue == null) {
            this.booleanValue = this.getParser().getAttributeBooleanValue(this.getIndex(), defaultValue);
        }

        return this.booleanValue;
    }

    @Override
    public int getResourceValue(final int defaultValue) throws IllegalStateException {
        if (this.resourceValue == NOT_SET) {
            this.resourceValue = this.getParser().getAttributeResourceValue(this.getIndex(), defaultValue);
        }

        return this.resourceValue;
    }

    @Override
    public int getIntValue(final int defaultValue) throws IllegalStateException {
        if (this.intValue == NOT_SET) {
            this.intValue = this.getParser().getAttributeIntValue(this.getIndex(), defaultValue);
        }

        return this.intValue;
    }

    @Override
    public int getUnsignedIntValue(final int defaultValue) throws IllegalStateException {
        if (this.unsignedIntValue == NOT_SET) {
            this.unsignedIntValue = this.getParser().getAttributeUnsignedIntValue(this.getIndex(), defaultValue);
        }

        return this.unsignedIntValue;
    }

    @Override
    public float getFloatValue(final float defaultValue) throws IllegalStateException {
        if (this.floatValue == NOT_SET) {
            this.floatValue = this.getParser().getAttributeFloatValue(this.getIndex(), defaultValue);
        }

        return this.floatValue;
    }
}
