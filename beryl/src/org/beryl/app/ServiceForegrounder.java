package org.beryl.app;

import java.lang.reflect.Method;

import org.beryl.diagnostics.ExceptionReporter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

/**
 * Sets an Android Service into foreground state with a notification.
 * Since 2.0, Android requires that all foregrounded service have a pending notification shown.
 * This class provides backwards compatibility to older Android handsets that do not have this API.
 */
public class ServiceForegrounder {

	private final IServiceForegrounder foregrounderProxy;
	private final Service _service;
	private final int _notificationId;
	private boolean _isForegrounded = false;


	public ServiceForegrounder(final Service service, final int notificationId) {
		_service = service;
		_notificationId = notificationId;

		if(AndroidVersion.isEclairOrHigher()) {
			foregrounderProxy = new EclairOrHigherServiceForegrounder();
		}
		else {
			foregrounderProxy = new DonutAndBelowServiceForegrounder();
		}
	}

	public void startForeground(final int resIconId, final int title, final int description, final int tickerText, final PendingIntent onClickIntent) {
		final CharSequence titleString = _service.getText(title);
		final CharSequence descriptionString = _service.getText(description);
		final CharSequence tickerTextString = _service.getText(tickerText);

		startForeground(resIconId, titleString, descriptionString, tickerTextString, onClickIntent);
	}

	public void startForeground(final int resIconId, final CharSequence title, final CharSequence description, final CharSequence tickerText, final PendingIntent onClickIntent) {

		final Notification notifier = new Notification();
		notifier.icon = resIconId;

		notifier.tickerText = tickerText;
		notifier.setLatestEventInfo(_service, title, description, onClickIntent);

		startForeground(notifier);
	}

	public void startForeground(final Notification notification) {
		_isForegrounded = true;
		foregrounderProxy.startForeground(_service, _notificationId, notification);
	}

	public boolean isForegrounded() {
		return _isForegrounded;
	}

	public void stopForeground() {
		foregrounderProxy.stopForeground(_service, _notificationId);
		_isForegrounded = false;
	}

	/* Versioned Proxies for foregrounding a service. */

	static interface IServiceForegrounder {
		void startForeground(final Service service, final int notificationId, final Notification notification);
		void stopForeground(final Service service, final int notificationId);
	}

	static class DonutAndBelowServiceForegrounder implements IServiceForegrounder
	{
		private static final Class<?>[] service_setForegroundSignature = new Class[] {
		    boolean.class};
		static final Method service_setForeground;

		static {
			Method try_service_setForeground = null;
			try {
				try_service_setForeground = Service.class.getMethod("setForeground", service_setForegroundSignature);
			} catch (Exception e) {
				ExceptionReporter.report(e);
				try_service_setForeground = null;
			}
			service_setForeground = try_service_setForeground;
		}

		private NotificationManager getNotificationManager(final Service service) {
			return  (NotificationManager) service.getSystemService(Service.NOTIFICATION_SERVICE);
		}

		private void setForeground(Service service, boolean mode) {
			Object[] service_setForeground_Args = new Object[1];
			service_setForeground_Args[0] = Boolean.valueOf(mode);

			try {
				service_setForeground.invoke(service, service_setForeground_Args);
			} catch(Exception e) {
				ExceptionReporter.report(e);
			}
		}

		public void startForeground(final Service service, final int notificationId, final Notification notification) {

			setForeground(service, true);
			final NotificationManager nm = getNotificationManager(service);
			notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;

			nm.notify(notificationId, notification);
		}

		public void stopForeground(final Service service, final int notificationId) {
			final NotificationManager nm = getNotificationManager(service);
			nm.cancel(notificationId);
			setForeground(service, false);
		}
	}

	static class EclairOrHigherServiceForegrounder implements IServiceForegrounder
	{
		public void startForeground(final Service service, final int notificationId, final Notification notification) {
			notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE;
			service.startForeground(notificationId, notification);
		}

		public void stopForeground(final Service service, final int notificationId) {
			service.stopForeground(true);
		}
	}
}
