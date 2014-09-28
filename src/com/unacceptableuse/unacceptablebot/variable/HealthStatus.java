package com.unacceptableuse.unacceptablebot.variable;

public class HealthStatus
{
	private String serviceName = "Unknown service";
	private boolean isCritical = false;
	private String glyph = "asterisk"; // See: http://getbootstrap.com/components/#glyphicons
	private String status = "unknown status";
	
	private String serviceColour = "default"; //default - grey / primary - blue / success - green / info - blue / warning - orange / danger - red
	
	
	public HealthStatus(String serviceName, String status)
	{
		this.serviceName = serviceName;
		this.status = status;
	}
	
	public HealthStatus(String serviceName, String status, String glyph)
	{
		this.serviceName = serviceName;
		this.status = status;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public boolean isCritical()
	{
		return isCritical;
	}

	public HealthStatus setCritical(boolean isCritical)
	{
		this.isCritical = isCritical;
		return this;
	}

	public String getGlyph()
	{
		return glyph;
	}

	public void setGlyph(String glyph)
	{
		this.glyph = glyph;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "serviceName=" + serviceName + ",isCritical="+isCritical + ",glyph=" + glyph + ",status=" + status+ ",serviceColour=" + serviceColour;
	}

	public String getServiceColour()
	{
		return isCritical ? "danger" : serviceColour;
	}

	public void setServiceColour(String serviceColour)
	{
		this.serviceColour = serviceColour;
	}
	
	
}
