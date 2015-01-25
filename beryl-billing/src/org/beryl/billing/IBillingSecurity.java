package org.beryl.billing;

public interface IBillingSecurity {

	/**
     * Compute your public key (that you got from the Android Market publisher site).
     *
     * Instead of just storing the entire literal string here embedded in the
     * program,  construct the key at runtime from pieces or
     * use bit manipulation (for example, XOR with some other string) to hide
     * the actual key.  The key itself is not secret information, but we don't
     * want to make it easy for an adversary to replace the public key with one
     * of their own and then fake messages from the server.
     *
     * Generally, encryption keys / passwords should only be kept in memory
     * long enough to perform the operation they need to perform.
     */
	String getMarketPublicKey();
}
