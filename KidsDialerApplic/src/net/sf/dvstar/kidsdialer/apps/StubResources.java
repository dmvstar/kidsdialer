package net.sf.dvstar.kidsdialer.apps;

import net.sf.dvstar.kidsdialer.utils.Log;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;

public class StubResources extends Resources {

	private Resources mParentResources;

	public StubResources(Resources parentResources) {
		super(parentResources.getAssets(), parentResources.getDisplayMetrics(),
				parentResources.getConfiguration());
		this.mParentResources = parentResources;
	}

	@Override
	public String getString(int id) throws NotFoundException {
		Log.v("StubResources [0x" + Integer.toHexString(id) + "]=("
				+ mParentResources.getString(id) + ")");
		return mParentResources.getString(id);
	}

}
