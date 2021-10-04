package com.ibm.ssi.controller.company.util.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.validation.Valid;

public class ValidatableList<E> implements List<E> {

  @Valid
  private List<E> list;

  public ValidatableList() {
      this.list = new ArrayList<E>();
  }

  public ValidatableList(List<E> list) {
      this.list = list;
  }

  public List<E> getList() {
      return list;
  }

  public void setList(List<E> list) {
      this.list = list;
  }

  @Override
  public int size() {
      return list.size();
  }

  @Override
  public boolean isEmpty() {
      return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return this.list.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return this.list.iterator();
  }

  @Override
  public Object[] toArray() {
    return this.list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return this.list.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return this.list.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return this.list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return this.list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return this.list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return this.list.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return this.list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return this.list.retainAll(c);
  }

  @Override
  public void clear() {
    this.list.clear();
  }

  @Override
  public E get(int index) {
    return this.list.get(index);
  }

  @Override
  public E set(int index, E element) {
    return this.list.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    this.list.add(index, element);;
  }

  @Override
  public E remove(int index) {
    return this.list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return this.list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return this.list.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return this.list.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return this.list.listIterator(index);
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return this.list.subList(fromIndex, toIndex);
  }
}