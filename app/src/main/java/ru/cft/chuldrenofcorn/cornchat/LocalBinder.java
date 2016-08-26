package ru.cft.chuldrenofcorn.cornchat;
import android.os.Binder;

import java.lang.ref.WeakReference;


/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 14:41
 */

public class LocalBinder<S> extends Binder {
	private final WeakReference<S> mService;

	public LocalBinder(final S service) {
		mService = new WeakReference<S>(service);
	}

	public S getService() {
		return mService.get();
	}

}
