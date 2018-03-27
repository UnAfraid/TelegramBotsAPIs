/*
 * Copyright (c) 2017 Rumen Nikiforov <unafraid89@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.unafraid.telegrambot.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author UnAfraid
 */
public final class MapUtil
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);
	private final Map<String, Object> values;
	
	public MapUtil()
	{
		values = new LinkedHashMap<>();
	}
	
	public MapUtil(Map<String, Object> map)
	{
		values = map;
	}
	
	/**
	 * Returns the set of values
	 * @return HashMap
	 */
	public final Map<String, Object> getInternalMap()
	{
		return values;
	}
	
	/**
	 * Add a set of couple values in the current set
	 * @param newSet : MapSet pointing out the list of couples to add in the current set
	 */
	public void merge(MapUtil newSet)
	{
		Map<String, Object> newMap = newSet.getInternalMap();
		for (Entry<String, Object> entry : newMap.entrySet())
		{
			values.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Puts key-value pair inside the internal map, replacing previously existing key
	 * @param name
	 * @param value
	 * @return the same instance of current MapSet so it can be chained like:<br />
	 *         mapSet.put("key", "value").put("another key", "another value");
	 */
	public MapUtil put(String name, Object value)
	{
		if (value != null)
		{
			values.put(name, value);
		}
		return this;
	}
	
	/**
	 * Return the boolean associated to the key put in parameter ("name")
	 * @param name : String designating the key in the set
	 * @return boolean : value associated to the key
	 */
	public boolean getBoolean(String name)
	{
		Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Boolean value required, but not specified");
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue();
		}
		try
		{
			return Boolean.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Boolean value required, but found: " + val);
		}
	}
	
	/**
	 * Return the boolean associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : boolean designating the default value if value associated with the key is null
	 * @return boolean : value of the key
	 */
	public boolean getBoolean(String name, boolean defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue();
		}
		try
		{
			return Boolean.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Boolean value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the int associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : byte designating the default value if value associated with the key is null
	 * @return byte : value associated to the key
	 */
	public byte getByte(String name, byte defaultValue)
	{
		Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Number)
		{
			return ((Number) val).byteValue();
		}
		try
		{
			return Byte.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Byte value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the byte associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return byte : value associated to the key
	 */
	public byte getByte(String name)
	{
		Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Byte value required, but not specified");
		}
		if (val instanceof Number)
		{
			return ((Number) val).byteValue();
		}
		try
		{
			return Byte.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Byte value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the short associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : short designating the default value if value associated with the key is null
	 * @return short : value associated to the key
	 */
	public short getShort(String name, short defaultValue)
	{
		Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Number)
		{
			return ((Number) val).shortValue();
		}
		try
		{
			return Short.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Short value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the short associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return short : value associated to the key
	 */
	public short getShort(String name)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Short value required, but not specified");
		}
		if (val instanceof Number)
		{
			return ((Number) val).shortValue();
		}
		try
		{
			return Short.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Short value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the int associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return int : value associated to the key
	 */
	public int getInt(String name)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Integer value required, but not specified");
		}
		if (val instanceof Number)
		{
			return ((Number) val).intValue();
		}
		try
		{
			return Integer.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the int associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : int designating the default value if value associated with the key is null
	 * @return int : value associated to the key
	 */
	public int getInteger(String name, int defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Number)
		{
			return ((Number) val).intValue();
		}
		try
		{
			return Integer.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the int[] associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param delimiter
	 * @return int[] : value associated to the key
	 */
	public int[] getIntArray(String name, String delimiter)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return null;
		}
		if (val instanceof Number)
		{
			int[] result =
			{
				((Number) val).intValue()
			};
			return result;
		}
		
		int c = 0;
		String[] vals = ((String) val).split(delimiter);
		int[] result = new int[vals.length];
		for (String v : vals)
		{
			try
			{
				result[c++] = Integer.valueOf(v);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Integer value required, but found: " + val);
			}
		}
		return result;
	}
	
	public float[] getFloatArray(String name, String delimiter)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return null;
		}
		if (val instanceof Number)
		{
			float[] result =
			{
				((Number) val).floatValue()
			};
			return result;
		}
		int c = 0;
		String[] vals = ((String) val).split(delimiter);
		float[] result = new float[vals.length];
		for (String v : vals)
		{
			try
			{
				result[c++] = Float.valueOf(v);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Float value required, but found: " + val);
			}
		}
		return result;
	}
	
	/**
	 * Returns the long associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return long : value associated to the key
	 */
	public long getLong(String name)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Integer value required, but not specified");
		}
		if (val instanceof Number)
		{
			return ((Number) val).longValue();
		}
		try
		{
			return Long.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the long associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : long designating the default value if value associated with the key is null
	 * @return long : value associated to the key
	 */
	public long getLong(String name, int defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Number)
		{
			return ((Number) val).longValue();
		}
		try
		{
			return Long.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the float associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return float : value associated to the key
	 */
	public float getFloat(String name)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Float value required, but not specified");
		}
		if (val instanceof Number)
		{
			return ((Number) val).floatValue();
		}
		try
		{
			return Float.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the float associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : float designating the default value if value associated with the key is null
	 * @return float : value associated to the key
	 */
	public float getFloat(String name, float defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Number)
		{
			return ((Number) val).floatValue();
		}
		try
		{
			return Float.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the double associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return double : value associated to the key
	 */
	public double getDouble(String name)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Float value required, but not specified");
		}
		if (val instanceof Number)
		{
			return ((Number) val).doubleValue();
		}
		try
		{
			return Double.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the double associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : float designating the default value if value associated with the key is null
	 * @return double : value associated to the key
	 */
	public double getDouble(String name, float defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (val instanceof Number)
		{
			return ((Number) val).doubleValue();
		}
		try
		{
			return Double.valueOf((String) val);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Float value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the String associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return String : value associated to the key
	 */
	public String getString(String name)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("String '" + name + "' value required, but not specified");
		}
		return String.valueOf(val);
	}
	
	/**
	 * Returns the String associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter defaultValue.
	 * @param name : String designating the key in the set
	 * @param defaultValue : String designating the default value if value associated with the key is null
	 * @return String : value associated to the key
	 */
	public String getString(String name, String defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		return String.valueOf(val);
	}
	
	/**
	 * Returns an enumeration of &lt;T&gt; from the set
	 * @param <T> : Class of the enumeration returned
	 * @param name : String designating the key in the set
	 * @param enumClass : Class designating the class of the value associated with the key in the set
	 * @return Enum<T>
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + " required, but not specified");
		}
		if (enumClass.isInstance(val))
		{
			return (T) val;
		}
		try
		{
			return Enum.valueOf(enumClass, String.valueOf(val));
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + " required, but found: " + val);
		}
	}
	
	/**
	 * Returns an enumeration of &lt;T&gt; from the set. If the enumeration is empty, the method returns the value of the parameter "defaultValue".
	 * @param <T> : Class of the enumeration returned
	 * @param name : String designating the key in the set
	 * @param enumClass : Class designating the class of the value associated with the key in the set
	 * @param defaultValue : <T> designating the value by default
	 * @return Enum<T>
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass, T defaultValue)
	{
		final Object val = values.get(name);
		if (val == null)
		{
			return defaultValue;
		}
		if (enumClass.isInstance(val))
		{
			return (T) val;
		}
		try
		{
			return Enum.valueOf(enumClass, String.valueOf(val));
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + "required, but found: " + val);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final <A> A getObject(String name, Class<A> type)
	{
		Object obj = values.get(name);
		if ((obj == null) || !type.isAssignableFrom(obj.getClass()))
		{
			return null;
		}
		
		return (A) obj;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key, Class<T> clazz)
	{
		Objects.requireNonNull(key);
		Objects.requireNonNull(clazz);
		final Object obj = values.get(key);
		if ((obj == null) || !(obj instanceof List<?>))
		{
			return null;
		}
		
		final List<Object> originalList = (List<Object>) obj;
		if (!originalList.isEmpty() && !originalList.stream().allMatch(clazz::isInstance))
		{
			if (clazz.getSuperclass() == Enum.class)
			{
				throw new IllegalAccessError("Please use getEnumList if you want to get list of Enums!");
			}
			
			// Attempt to convert the list
			final List<T> convertedList = convertList(originalList, clazz);
			if (convertedList == null)
			{
				LOGGER.warn("getList(\"{}\", {}) requested with wrong generic type: {}!", key, clazz.getSimpleName(), obj.getClass().getGenericInterfaces()[0], new ClassNotFoundException());
				return null;
			}
			
			// Overwrite the existing list with proper generic type
			values.put(key, convertedList);
			return convertedList;
		}
		return (List<T>) obj;
	}
	
	public <T> List<T> getList(String key, Class<T> clazz, List<T> defaultValue)
	{
		final List<T> list = getList(key, clazz);
		return list == null ? defaultValue : list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Set<T> getSet(String key, Class<T> clazz)
	{
		Objects.requireNonNull(key);
		Objects.requireNonNull(clazz);
		final Object obj = values.get(key);
		if ((obj == null) || !(obj instanceof Set<?>))
		{
			return null;
		}
		
		final Set<Object> originalSet = (Set<Object>) obj;
		if (!originalSet.isEmpty() && !originalSet.stream().allMatch(clazz::isInstance))
		{
			if (clazz.getSuperclass() == Enum.class)
			{
				throw new IllegalAccessError("Please use getEnumSet if you want to get set of Enums!");
			}
			
			// Attempt to convert the set
			final Set<T> convertedSet = convertSet(originalSet, clazz);
			if (convertedSet == null)
			{
				LOGGER.warn("getSet(\"{}\", {}) requested with wrong generic type: {}!", key, clazz.getSimpleName(), obj.getClass().getGenericInterfaces()[0], new ClassNotFoundException());
				return null;
			}
			
			// Overwrite the existing set with proper generic type
			values.put(key, convertedSet);
			return convertedSet;
		}
		return (Set<T>) obj;
	}
	
	public <T> Set<T> getSet(String key, Class<T> clazz, Set<T> defaultValue)
	{
		final Set<T> set = getSet(key, clazz);
		return set == null ? defaultValue : set;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> List<T> getEnumList(String key, Class<T> clazz)
	{
		final Object obj = values.get(key);
		if ((obj == null) || !(obj instanceof List<?>))
		{
			return null;
		}
		
		final List<Object> originalList = (List<Object>) obj;
		if (!originalList.isEmpty() && (obj.getClass().getGenericInterfaces()[0] != clazz) && originalList.stream().allMatch(name -> CommonUtil.isEnum(name.toString(), clazz)))
		{
			final List<T> convertedList = originalList.stream().map(Object::toString).map(name -> Enum.valueOf(clazz, name)).map(clazz::cast).collect(Collectors.toList());
			
			// Overwrite the existing list with proper generic type
			values.put(key, convertedList);
			return convertedList;
		}
		return (List<T>) obj;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> Set<T> getEnumSet(String key, Class<T> clazz)
	{
		final Object obj = values.get(key);
		if ((obj == null) || !(obj instanceof List<?>))
		{
			return null;
		}
		
		final Set<Object> originalSet = (Set<Object>) obj;
		if (!originalSet.isEmpty() && (obj.getClass().getGenericInterfaces()[0] != clazz) && originalSet.stream().allMatch(name -> CommonUtil.isEnum(name.toString(), clazz)))
		{
			final Set<T> convertedSet = originalSet.stream().map(Object::toString).map(name -> Enum.valueOf(clazz, name)).map(clazz::cast).collect(Collectors.toSet());
			
			// Overwrite the existing set with proper generic type
			values.put(key, convertedSet);
			return convertedSet;
		}
		return (Set<T>) obj;
	}
	
	/**
	 * @param <T>
	 * @param originalList
	 * @param clazz
	 * @return
	 */
	private <T> List<T> convertList(List<Object> originalList, Class<T> clazz)
	{
		if (clazz == Integer.class)
		{
			if (originalList.stream().map(Object::toString).allMatch(CommonUtil::isInteger))
			{
				return originalList.stream().map(Object::toString).map(Integer::valueOf).map(clazz::cast).collect(Collectors.toList());
			}
		}
		else if (clazz == Float.class)
		{
			if (originalList.stream().map(Object::toString).allMatch(CommonUtil::isFloat))
			{
				return originalList.stream().map(Object::toString).map(Float::valueOf).map(clazz::cast).collect(Collectors.toList());
			}
		}
		else if (clazz == Double.class)
		{
			if (originalList.stream().map(Object::toString).allMatch(CommonUtil::isDouble))
			{
				return originalList.stream().map(Object::toString).map(Double::valueOf).map(clazz::cast).collect(Collectors.toList());
			}
		}
		return null;
	}
	
	/**
	 * @param <T>
	 * @param originalSet
	 * @param clazz
	 * @return
	 */
	private <T> Set<T> convertSet(Set<Object> originalSet, Class<T> clazz)
	{
		if (clazz == Integer.class)
		{
			if (originalSet.stream().map(Object::toString).allMatch(CommonUtil::isInteger))
			{
				return originalSet.stream().map(Object::toString).map(Integer::valueOf).map(clazz::cast).collect(Collectors.toSet());
			}
		}
		else if (clazz == Float.class)
		{
			if (originalSet.stream().map(Object::toString).allMatch(CommonUtil::isFloat))
			{
				return originalSet.stream().map(Object::toString).map(Float::valueOf).map(clazz::cast).collect(Collectors.toSet());
			}
		}
		else if (clazz == Double.class)
		{
			if (originalSet.stream().map(Object::toString).allMatch(CommonUtil::isDouble))
			{
				return originalSet.stream().map(Object::toString).map(Double::valueOf).map(clazz::cast).collect(Collectors.toSet());
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String key, Class<K> keyClass, Class<V> valueClass)
	{
		final Object obj = values.get(key);
		if ((obj == null) || !(obj instanceof Map<?, ?>))
		{
			return null;
		}
		
		final Map<?, ?> originalList = (Map<?, ?>) obj;
		if (!originalList.isEmpty())
		{
			if ((!originalList.keySet().stream().allMatch(keyClass::isInstance)) || (!originalList.values().stream().allMatch(valueClass::isInstance)))
			{
				LOGGER.warn("getMap(\"{}\", {}, {}) requested with wrong generic type: {}!", key, keyClass.getSimpleName(), valueClass.getSimpleName(), obj.getClass().getGenericInterfaces()[0], new ClassNotFoundException());
			}
		}
		return (Map<K, V>) obj;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, List<V>> getMapOfList(String key, Class<K> keyClass, Class<V> valueClass)
	{
		final Object obj = values.get(key);
		if ((obj == null) || !(obj instanceof Map<?, ?>))
		{
			return null;
		}
		
		final Map<?, ?> originalList = (Map<?, ?>) obj;
		if (!originalList.isEmpty())
		{
			if ((!originalList.keySet().stream().allMatch(keyClass::isInstance)) || (!originalList.values().stream().allMatch(List.class::isInstance)))
			{
				LOGGER.warn("getMap(\"{}\", {}, {}) requested with wrong generic type: {}!", key, keyClass.getSimpleName(), valueClass.getSimpleName(), obj.getClass().getGenericInterfaces()[0], new ClassNotFoundException());
			}
		}
		return (Map<K, List<V>>) obj;
	}
}
