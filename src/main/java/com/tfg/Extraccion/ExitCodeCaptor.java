package com.tfg.Extraccion;

import java.security.Permission;

class ExitCodeCaptor extends SecurityManager implements AutoCloseable {
	private final SecurityManager existingSecurityManager;
	private int status = 0;	
	ExitCodeCaptor() {
		this.existingSecurityManager = System.getSecurityManager();
		System.setSecurityManager(this);
	}

	@Override
	public void checkExit(int status) {
		this.status = status;
		throw new SecurityExceptionCheckExit();
	}

	@Override
	public void checkPermission(Permission perm) {
		if (existingSecurityManager != null)
			existingSecurityManager.checkPermission(perm);
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		if (existingSecurityManager != null)
			existingSecurityManager.checkPermission(perm, context);
	}

	@Override
	public void close() {
		System.setSecurityManager(existingSecurityManager);
	}

	void run(Runnable runnable) {
		try {
			runnable.run();
		} catch (SecurityExceptionCheckExit ignore) { }
	}

	int getStatus() {
		return status;
	}

	private static class SecurityExceptionCheckExit extends SecurityException {
		private static final long serialVersionUID = 1L;
	}
}