package com.minersstudios.genero.lib.xml.tag.attribute.reader;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.xml.tag.attribute.Attribute;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Reader<T extends Attribute> extends Iterable<T> {

    /**
     * Returns an iterator over the attributes
     *
     * @return An iterator over the attributes
     */
    @Override
    @NonNull Iterator<T> iterator();

    /**
     * Returns a spliterator over the attributes
     *
     * @return A spliterator over the attributes
     */
    @Override
    @NonNull Spliterator<T> spliterator();

    /**
     * Returns the attribute at the given index
     *
     * @param index The index of the attribute
     * @return The attribute at the given index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     * @throws IllegalStateException     If the parser is in an invalid state
     */
    @NonNull T getAttribute(final int index) throws IndexOutOfBoundsException, IllegalStateException;

    /**
     * Returns the number of attributes
     *
     * @return The number of attributes
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int count() throws IllegalStateException;

    /**
     * Returns whether the reader has no attributes
     *
     * @return Whether the reader has no attributes
     * @throws IllegalStateException If the parser is in an invalid state
     */
    boolean isEmpty() throws IllegalStateException;

    /**
     * Returns whether the reader contains the given attribute
     *
     * @param attribute The attribute to check for
     * @return Whether the reader contains the given attribute
     * @throws IllegalStateException If the parser is in an invalid state
     * @see #contains(String, String)
     */
    boolean contains(final @NonNull T attribute) throws IllegalStateException;

    /**
     * Returns whether the reader contains the attribute with the given
     * namespace and name
     *
     * @param namespace The namespace of the attribute
     * @param name      The name of the attribute
     * @return Whether the reader contains the attribute with the given
     *         namespace and name
     * @throws IllegalStateException If the parser is in an invalid state
     */
    boolean contains(
            final @NonNull String namespace,
            final @NonNull String name
    ) throws IllegalStateException;

    /**
     * Returns whether the reader contains all of the given attributes
     *
     * @param attributes The attributes to check for
     * @return Whether the reader contains all of the given attributes
     * @throws IllegalStateException If the parser is in an invalid state
     * @see #contains(Attribute)
     */
    boolean containsAll(final @NonNull Iterable<T> attributes) throws IllegalStateException;

    /**
     * Returns whether the reader contains any of the given attributes
     *
     * @param attributes The attributes to check for
     * @return Whether the reader contains any of the given attributes
     * @throws IllegalStateException If the parser is in an invalid state
     * @see #contains(Attribute)
     */
    boolean containsAny(final @NonNull Iterable<T> attributes) throws IllegalStateException;

    /**
     * Performs the given action for each attribute until all attributes have
     * been processed or the action throws an exception
     *
     * @param action The action to be performed for each attribute
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @Override
    void forEach(final @NonNull Consumer<? super T> action) throws IllegalStateException;

    /**
     * Returns a stream of the attributes
     *
     * @return A stream of the attributes
     */
    @NonNull Stream<T> stream();

    /**
     * Returns a parallel stream of the attributes
     *
     * @return A parallel stream of the attributes
     */
    @NonNull Stream<T> parallelStream();
}
