package com.minersstudios.genero.lib.xml.tag.attribute.reader;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.xml.tag.attribute.Attribute;

import org.xmlpull.v1.XmlPullParser;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.concurrent.Immutable;

@Immutable
abstract class ReaderImpl<A extends Attribute, P extends XmlPullParser> implements Reader<A> {
    private final Reference<P> parser;

    ReaderImpl(final @NonNull P parser) {
        this.parser = new WeakReference<>(parser);
    }

    @Override
    public @NonNull Iterator<A> iterator() {
        return new Iterator<A>() {
            private int index = 0;

            @Override
            public boolean hasNext() throws IllegalStateException {
                return this.index < ReaderImpl.this.count();
            }

            @Override
            public @NonNull A next() throws NoSuchElementException {
                return ReaderImpl.this.getAttribute(this.index++);
            }
        };
    }

    @Override
    public @NonNull Spliterator<A> spliterator() {
        return Spliterators.spliteratorUnknownSize(
                this.iterator(),
                Spliterator.ORDERED | Spliterator.IMMUTABLE
        );
    }

    @Override
    public int count() throws IllegalStateException {
        return this.getParser().getAttributeCount();
    }

    @Override
    public boolean isEmpty() throws IllegalStateException {
        return this.count() <= 0;
    }

    @Override
    public boolean contains(final @NonNull A attribute) throws IllegalStateException {
        return this.contains(attribute.getNamespace(), attribute.getName());
    }

    @Override
    public boolean contains(
            final @NonNull String namespace,
            final @NonNull String name
    ) throws IllegalStateException {
        return this.getParser().getAttributeValue(namespace, name) != null;
    }

    @Override
    public boolean containsAll(final @NonNull Iterable<A> attributes) throws IllegalStateException {
        for (final A attribute : attributes) {
            if (!this.contains(attribute)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean containsAny(final @NonNull Iterable<A> attributes) throws IllegalStateException {
        for (final A attribute : attributes) {
            if (this.contains(attribute)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void forEach(final @NonNull Consumer<? super A> action) throws IllegalStateException {
        for (final A attribute : this) {
            action.accept(attribute);
        }
    }

    @Override
    public @NonNull Stream<A> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public @NonNull Stream<A> parallelStream() {
        return StreamSupport.stream(this.spliterator(), true);
    }

    protected @NonNull P getParser() throws IllegalStateException {
        final P parser = this.parser.get();

        if (parser == null) {
            throw new IllegalStateException("Parser is in an invalid state");
        }

        return parser;
    }
}
