/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.kromatik.dasshy.thrift.model;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2016-06-13")
public class TStagePluginList implements org.apache.thrift.TBase<TStagePluginList, TStagePluginList._Fields>, java.io.Serializable, Cloneable, Comparable<TStagePluginList> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TStagePluginList");

  private static final org.apache.thrift.protocol.TField PLUGINS_FIELD_DESC = new org.apache.thrift.protocol.TField("plugins", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TStagePluginListStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TStagePluginListTupleSchemeFactory());
  }

  private List<TStagePlugin> plugins; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PLUGINS((short)1, "plugins");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // PLUGINS
          return PLUGINS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PLUGINS, new org.apache.thrift.meta_data.FieldMetaData("plugins", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TStagePlugin.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TStagePluginList.class, metaDataMap);
  }

  public TStagePluginList() {
  }

  public TStagePluginList(
    List<TStagePlugin> plugins)
  {
    this();
    this.plugins = plugins;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TStagePluginList(TStagePluginList other) {
    if (other.isSetPlugins()) {
      List<TStagePlugin> __this__plugins = new ArrayList<TStagePlugin>(other.plugins.size());
      for (TStagePlugin other_element : other.plugins) {
        __this__plugins.add(new TStagePlugin(other_element));
      }
      this.plugins = __this__plugins;
    }
  }

  public TStagePluginList deepCopy() {
    return new TStagePluginList(this);
  }

  @Override
  public void clear() {
    this.plugins = null;
  }

  public int getPluginsSize() {
    return (this.plugins == null) ? 0 : this.plugins.size();
  }

  public java.util.Iterator<TStagePlugin> getPluginsIterator() {
    return (this.plugins == null) ? null : this.plugins.iterator();
  }

  public void addToPlugins(TStagePlugin elem) {
    if (this.plugins == null) {
      this.plugins = new ArrayList<TStagePlugin>();
    }
    this.plugins.add(elem);
  }

  public List<TStagePlugin> getPlugins() {
    return this.plugins;
  }

  public void setPlugins(List<TStagePlugin> plugins) {
    this.plugins = plugins;
  }

  public void unsetPlugins() {
    this.plugins = null;
  }

  /** Returns true if field plugins is set (has been assigned a value) and false otherwise */
  public boolean isSetPlugins() {
    return this.plugins != null;
  }

  public void setPluginsIsSet(boolean value) {
    if (!value) {
      this.plugins = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PLUGINS:
      if (value == null) {
        unsetPlugins();
      } else {
        setPlugins((List<TStagePlugin>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PLUGINS:
      return getPlugins();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PLUGINS:
      return isSetPlugins();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TStagePluginList)
      return this.equals((TStagePluginList)that);
    return false;
  }

  public boolean equals(TStagePluginList that) {
    if (that == null)
      return false;

    boolean this_present_plugins = true && this.isSetPlugins();
    boolean that_present_plugins = true && that.isSetPlugins();
    if (this_present_plugins || that_present_plugins) {
      if (!(this_present_plugins && that_present_plugins))
        return false;
      if (!this.plugins.equals(that.plugins))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_plugins = true && (isSetPlugins());
    list.add(present_plugins);
    if (present_plugins)
      list.add(plugins);

    return list.hashCode();
  }

  @Override
  public int compareTo(TStagePluginList other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetPlugins()).compareTo(other.isSetPlugins());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPlugins()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.plugins, other.plugins);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TStagePluginList(");
    boolean first = true;

    sb.append("plugins:");
    if (this.plugins == null) {
      sb.append("null");
    } else {
      sb.append(this.plugins);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TStagePluginListStandardSchemeFactory implements SchemeFactory {
    public TStagePluginListStandardScheme getScheme() {
      return new TStagePluginListStandardScheme();
    }
  }

  private static class TStagePluginListStandardScheme extends StandardScheme<TStagePluginList> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TStagePluginList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PLUGINS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list26 = iprot.readListBegin();
                struct.plugins = new ArrayList<TStagePlugin>(_list26.size);
                TStagePlugin _elem27;
                for (int _i28 = 0; _i28 < _list26.size; ++_i28)
                {
                  _elem27 = new TStagePlugin();
                  _elem27.read(iprot);
                  struct.plugins.add(_elem27);
                }
                iprot.readListEnd();
              }
              struct.setPluginsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TStagePluginList struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.plugins != null) {
        oprot.writeFieldBegin(PLUGINS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.plugins.size()));
          for (TStagePlugin _iter29 : struct.plugins)
          {
            _iter29.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TStagePluginListTupleSchemeFactory implements SchemeFactory {
    public TStagePluginListTupleScheme getScheme() {
      return new TStagePluginListTupleScheme();
    }
  }

  private static class TStagePluginListTupleScheme extends TupleScheme<TStagePluginList> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TStagePluginList struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetPlugins()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetPlugins()) {
        {
          oprot.writeI32(struct.plugins.size());
          for (TStagePlugin _iter30 : struct.plugins)
          {
            _iter30.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TStagePluginList struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list31 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.plugins = new ArrayList<TStagePlugin>(_list31.size);
          TStagePlugin _elem32;
          for (int _i33 = 0; _i33 < _list31.size; ++_i33)
          {
            _elem32 = new TStagePlugin();
            _elem32.read(iprot);
            struct.plugins.add(_elem32);
          }
        }
        struct.setPluginsIsSet(true);
      }
    }
  }

}

