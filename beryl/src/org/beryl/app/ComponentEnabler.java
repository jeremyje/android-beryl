package org.beryl.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/** Enables and disables component classes from the AndroidManifest.xml
 * Settings are persistent across reboots.
 * Useful for disabling components to save power. */
public class ComponentEnabler {

	private final Context context;
	private final PackageManager pm;
	
	public ComponentEnabler(final Context context) {
		this.context = context;
		this.pm = context.getPackageManager();
	}
	
	protected ComponentName toComponentName(final Class<?> componentClass) {
		return new ComponentName(context, componentClass);
	}
	
	/** Sets the component to its default state. */
	public void setDefault(final Class<?> componentClass) {
		setDefault(toComponentName(componentClass));
	}
	
	/** Sets the component to its default state. */
	public void setDefault(final ComponentName componentName) {
		pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
	}
	
	/** Sets the enabled state of the component based on the enabled boolean. */
	public void toggle(final Class<?> componentClass, boolean enabled) {
		toggle(toComponentName(componentClass), enabled);
	}
	
	/** Sets the enabled state of the component based on the enabled boolean. */
	public void toggle(final ComponentName componentName, boolean enabled) {
		if(enabled) {
			enable(componentName);
		} else {
			disable(componentName);
		}
	}
	
	/** Enables the component. */
	public void enable(final Class<?> componentClass) {
		enable(toComponentName(componentClass));
	}
	
	/** Enables the component. */
	public void enable(final ComponentName componentName) {
		pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}
	
	/** Disables the component. */
	public void disable(final Class<?> componentClass) {
		disable(toComponentName(componentClass));
	}
	
	/** Disables the component. */
	public void disable(final ComponentName componentName) {
		pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}
	
	/** Returns true if the component is enabled. */
	public boolean isEnabled(final Class<?> componentClass) {
		return isEnabled(toComponentName(componentClass));
	}
	
	/** Returns true if the component is enabled. */
	public boolean isEnabled(final ComponentName componentName) {
		
		final int state = pm.getComponentEnabledSetting(componentName);
		
		switch(state) {
			case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER:
			case PackageManager.COMPONENT_ENABLED_STATE_DISABLED: {
				return false;
			}
			case PackageManager.COMPONENT_ENABLED_STATE_ENABLED: {
				return true;
			}
			case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT: {
				return getDefaultState(componentName);
			}
		}
		
		return false;
	}

	/** Returns true if the default state (as declared in the AndroidManifest.xml) is enabled for this component. */
	public boolean getDefaultState(final Class<?> componentClass) {
		return getDefaultState(toComponentName(componentClass));
	}
	
	/** Returns true if the default state (as declared in the AndroidManifest.xml) is enabled for this component. */
	public boolean getDefaultState(final ComponentName componentName) {
		boolean result = false;
		try {
			final ComponentInfo info = pm.getActivityInfo(componentName, 0);
			result = info.enabled;
		} catch (NameNotFoundException e) {
			result = false;
		}
		return result;
	}
}
