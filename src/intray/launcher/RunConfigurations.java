package intray.launcher;

import java.lang.reflect.Field;

public class RunConfigurations {
	public boolean forceFirstRun = true;

	public void printFieldNamesAndValues() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("RunConfigurations: ");
		for (Field field : RunConfigurations.this.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String name = field.getName();
			Object value = field.get(RunConfigurations.this);
			System.out.printf("\t%s: %s%n", name, value);
		}
		System.out.println("\n\n");
	}
}
