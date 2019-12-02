
package com.platform.facade;

public interface PayWorkCallable<E> {
	public void onSuccess(E obj);
	public void onFail(E obj);
}
  
