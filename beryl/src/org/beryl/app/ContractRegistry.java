package org.beryl.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.beryl.diagnostics.ExceptionReporter;
import org.beryl.util.Reflection;

/**
Registry of interfaces that can be queried by other objects. The primary goal is to provide a way to decouple {@link android.app.Fragment}s while allowing them to communicate with each other through these exposed interfaces.
This class is not specific to Activities and Fragments but can be used in any situation where multiple components need to communicate with each other.

<h2>Pattern Setup</h2>
<ol>
	<li>Create a class that implements the {@link org.beryl.app.IContractMediator} interface. This class will hold the ContractRegistry object.</li>
	<li>Determine and create the contracts (interface) that the classes would want exposed. These interfaces must extend {@link org.beryl.app.RegisterableContract}.</li>
	<li>For each component, create non-static inner classes for the each interface they will expose. This is preferred over having the component itself implement the interface but that is supported as well.</li>
	<li>When wiring up the objects together get the handle of the ContractsRegistry from the class that implements {@link org.beryl.app.IContractMediator}. Add the object to the registry via the .add(Object) method.</li>
	<li>Likewise when removing the object call .remove(object) against the registry otherwise a GC memory-leak will occur.</li>
</ol>
<div class="diagram">
<img src="doc-files/ContractRegistry.png" />
Figure illustrates how the components are connected to each other via the ContractRegistry and their respective Contracts.
</div>
<h2>Example</h2>
<pre class="code"><code class="java">

public class MainActivity extends FragmentActivity implements IContractMediator {
	final private ContractRegistry contracts = new ContractRegistry();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contracts.add(this);
	}

	public ContractRegistry getContractRegistry() {
		return contracts;
	}
}

public interface IDoWorkCommand extends RegisterableContract {
	void doWork();
}

public class WorkerFragment extends Fragment {

	ContractRegistry contractSource = null;

	// Standard boilerplate code for attaching/detaching the Contract members of code.
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		contractSource = ContractRegistry.getContractRegistry(activity);
		contractSource.add(this);
	}

	public void onDetach() {
		super.onDetach();
		contractSource.remove(this);
		contractSource = null;
	}

	final IDoWorkCommand fragmentISavePosterCommand = new IDoWorkCommand() {
		public void doWork() {
			// Do something.
		}
	};
}

public class RequesterFragment extends Fragment {

	// Standard boilerplate code for attaching/detaching the Contract members of code.
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		contractSource = ContractRegistry.getContractRegistry(activity);
		contractSource.add(this);
	}

	public void onDetach() {
		super.onDetach();
		contractSource.remove(this);
		contractSource = null;
	}

	public void requestWork() {
		final ArrayList<IDoWorkCommand> listeners = contractSource.getAll(IDoWorkCommand);
		for(IDoWorkCommand cmd : cmds) {
			cmd.doWork();
		}
	}
}
</code></pre>*/
public final class ContractRegistry {

	private final HashMap<Class<?>, ArrayList<?>> contractsContainer = new HashMap<Class<?>, ArrayList<?>>();

	public ContractRegistry() {
	}

	/** Adds an object and it's members variables to the registry if they implement a RegisterableContact interface. */
	public void add(Object object) {
		attach(object);
		attachMembers(object);
	}

	/** Removes the an object and its members from the registry. */
	public void remove(Object object) {
		detach(object);
		detachMembers(object);
	}

	@SuppressWarnings("unchecked")
	private void attach(Object object) {
		Class<?> clazz = object.getClass();
		Class<?> [] ifaces = Reflection.getAllInterfaces(clazz);
		for(Class<?> iface : ifaces) {
			if(isRegisterableContractInterface(iface)) {
				@SuppressWarnings("rawtypes")
				ArrayList/* <LOL> */ contracts = getAll(iface);

				if(! contracts.contains(object)) {
					contracts.add(object);
				}
			}
		}
	}

	private void detach(Object object) {
		for(Class<?> key : contractsContainer.keySet()) {
			final ArrayList<?> contractList = contractsContainer.get(key);
			contractList.remove(object);
		}
	}

	private void attachMembers(Object obj) {
		final ArrayList<Field> fields = getContractFields(obj);
		for(Field field : fields) {
			try {
				Object contractMember = field.get(obj);
				if(contractMember != null) {
					attach(contractMember);
				}
			} catch (Exception e) {
				ExceptionReporter.report(e);
			}
		}
	}

	private void detachMembers(Object obj) {
		final ArrayList<Field> fields = getContractFields(obj);
		for(Field field : fields) {
			try {
				detach(field.get(obj));
			} catch (Exception e) {
				ExceptionReporter.report(e);
			}
		}
	}

	private boolean isRegisterableContractInterface(Class<?> clazz) {
		return hasInterface(clazz, RegisterableContract.class);
	}

	private boolean hasInterface(Class<?> target, Class<?> iface) {
		return iface.isAssignableFrom(target);
	}

	public <T> T get(Class<T> clazz) {
		final ArrayList<T> elements = getAll(clazz);

		if(! elements.isEmpty()) {
			return elements.get(0);
		} else {
			return null;
		}
	}

	/** Returns all instances of the contract class. */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAll(Class<T> clazz) {
		ArrayList<T> results = (ArrayList<T>)contractsContainer.get(clazz);

		if(results == null) {
			results = createContractList(clazz);
		}

		return results;
	}

	private <T> ArrayList<T> createContractList(Class<T> clazz) {
		final ArrayList<T> results = new ArrayList<T>();
		contractsContainer.put(clazz, results);
		return results;
	}

	private ArrayList<Field> getContractFields(Object obj) {
		ArrayList<Field> result = new ArrayList<Field>();
		Class<?> clazz = obj.getClass();
		Field [] fields = clazz.getDeclaredFields();

		for(Field field : fields) {
			field.setAccessible(true);
			Class<?> fieldType = field.getType();

			if(isRegisterableContractInterface(fieldType)) {
				result.add(field);
			}
		}

		return result;
	}

	public static ContractRegistry getContractRegistry(Object maybeContractMediator) {
		ContractRegistry registry = null;
		try {
			registry = ((IContractMediator)maybeContractMediator).getContractRegistry();
		} catch(ClassCastException e) {
			ExceptionReporter.report(e);
			throw new ClassCastException("Parameter must implement interface, " + IContractMediator.class.getName());
		}

		return registry;
	}
}
