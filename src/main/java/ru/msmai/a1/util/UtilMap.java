package ru.msmai.a1.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UtilMap {

	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> toMap(T... args){
		Map<String, T> result = new HashMap<>(args==null?1: args.length/2);
		if( args != null ){
			for(int i=0,max=args.length; i<max; i+=2){
				result.put(args[i].toString(), args[i+1]);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> toMap(Map<?,T>...args){
		Map<String, T> result = new HashMap<>();
		if( args != null ){
			Arrays.stream(args).forEach(arr->{
				arr.entrySet().stream().forEach(entry->{
					T value = entry.getValue();
					if( value instanceof Map ){
						result.putAll(toMap(value));
					} else {
						result.put(entry.getKey().toString(), value);
					}
				});
			});
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T>[] toMaps(){
		return new HashMap[]{new HashMap<>()};
	}
}
