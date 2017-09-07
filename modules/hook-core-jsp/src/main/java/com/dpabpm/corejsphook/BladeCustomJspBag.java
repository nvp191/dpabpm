/**
 * 
 */

package com.dpabpm.corejsphook;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.liferay.portal.deploy.hot.CustomJspBag;
import com.liferay.portal.kernel.url.URLContainer;

/**
 * @author phucnv
 * @date Sep 7, 2017
 */
@Component(immediate = true, property = {
	"context.id=BladeCustomJspBag", "context.name=Custom JSP Bag",
	"service.ranking:Integer=100"
})
public class BladeCustomJspBag implements CustomJspBag {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.deploy.hot.CustomJspBag#getCustomJspDir()
	 */
	@Override
	public String getCustomJspDir() {

		return "META-INF/jsps/";
	}

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.deploy.hot.CustomJspBag#getCustomJsps()
	 */
	@Override
	public List<String> getCustomJsps() {

		return _customJsps;
	}

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.deploy.hot.CustomJspBag#getURLContainer()
	 */
	@Override
	public URLContainer getURLContainer() {

		return _urlContainer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.deploy.hot.CustomJspBag#isCustomJspGlobal()
	 */
	@Override
	public boolean isCustomJspGlobal() {

		return true;
	}

	/**
	 * Adds the URL paths for all custom core JSPs to a list when the module is
	 * activated.
	 *
	 * @param bundleContext
	 *            the bundle context from which to get the custom JSP's bundle
	 */
	@Activate
	protected void activate(BundleContext bundleContext) {

		_bundle = bundleContext.getBundle();

		_customJsps = new ArrayList<>();

		Enumeration<URL> entries =
			_bundle.findEntries(getCustomJspDir(), "*.jsp", true);

		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();

			_customJsps.add(url.getPath());

		}
	}

	private Bundle _bundle;
	private List<String> _customJsps;
	private final URLContainer _urlContainer = new URLContainer() {

		@Override
		public URL getResource(String name) {

			return _bundle.getEntry(name);
		}

		@Override
		public Set<String> getResources(String path) {

			Set<String> paths = new HashSet<>();

			for (String entry : _customJsps) {
				if (entry.startsWith(path)) {
					paths.add(entry);
				}
			}

			return paths;
		}

	};

}
