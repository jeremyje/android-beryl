package org.beryl.app;

/**
 * This interface is used to denote other interfaces as "contracts" that can be registered in the ContractRegistry.
 *
 * NOTE: There are no methods in this interface. And yes I know, this could have been an Annotation.
 * I didn't want to deal with weird runtime retention problems because of ProGuard.
 */
public interface RegisterableContract {}
