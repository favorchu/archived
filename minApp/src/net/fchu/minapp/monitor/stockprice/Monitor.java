package net.fchu.minapp.monitor.stockprice;

import net.fchu.minapp.monitor.EventAction;

public interface Monitor {

	public abstract String getDescription();

	public abstract void setOnUpdatedAction(EventAction onUpdatedAction);

}