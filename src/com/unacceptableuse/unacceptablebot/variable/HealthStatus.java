package com.unacceptableuse.unacceptablebot.variable;

public class HealthStatus
{
	private String glyph = "asterisk"; // See: http://getbootstrap.com/components/#glyphicons
	private boolean isCritical = false;
	private String serviceColour = "default"; // default - grey / primary - blue / success - green / info - blue / warning - orange / danger - red
	private String serviceName = "Unknown service";

	private String status = "unknown status";

	public HealthStatus(final String serviceName, final String status)
	{
		this.serviceName = serviceName;
		this.status = status;
	}

	public HealthStatus(final String serviceName, final String status, final String glyph)
	{
		this.serviceName = serviceName;
		this.status = status;
	}

	public String getGlyph()
	{
		return glyph;
	}

	public String getServiceColour()
	{
		return isCritical ? "danger" : serviceColour;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public String getStatus()
	{
		return status;
	}

	public boolean isCritical()
	{
		return isCritical;
	}

	public HealthStatus setCritical(final boolean isCritical)
	{
		this.isCritical = isCritical;
		return this;
	}

	public void setGlyph(final String glyph)
	{
		this.glyph = glyph;
	}

	public void setServiceColour(final String serviceColour)
	{
		this.serviceColour = serviceColour;
	}

	public void setServiceName(final String serviceName)
	{
		this.serviceName = serviceName;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "serviceName=" + serviceName + ",isCritical=" + isCritical + ",glyph=" + glyph + ",status=" + status + ",serviceColour=" + serviceColour;
	}

}
