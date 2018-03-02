package edomingues.restserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.message.internal.ReaderWriter;

public class CustomLoggingFilter extends LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter
{

    private static DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss.SSS");

    @Override
    public void filter(ContainerRequestContext requestContext)  throws IOException
    {
        StringBuilder sb = new StringBuilder();
        //sb.append(Logger.getLogger("com.sun.jersey").getLevel() + ":");
        sb.append("|DEBUG|");
        sb.append(requestContext.getProperty("REQUEST_ID"));
        sb.append("|").append(requestContext.getMethod());
        String tmp = requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown"
                : requestContext.getSecurityContext().getUserPrincipal().toString().replaceAll("\\|","||");
        sb.append("|User: ").append(tmp);
        sb.append("|Path: ").append(requestContext.getUriInfo().getPath().replaceAll("\\|","||"));
        sb.append("|Header: ").append(requestContext.getHeaders().toString().replaceAll("\\|","||"));
        sb.append("|Entity: [").append(getEntityBody(requestContext)).append("]|");
        System.out.println(ZonedDateTime.now(ZoneOffset.UTC).format(LOG_FORMAT) + " Z" + sb.toString());
    }

    private String getEntityBody(ContainerRequestContext requestContext)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = requestContext.getEntityStream();

        final StringBuilder b = new StringBuilder();
        try
        {
            ReaderWriter.writeTo(in, out);

            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0)
            {
                b.append("Test|field|marker|replacement");
            }
            else
            {
                b.append(new String(requestEntity));
            }
            requestContext.setEntityStream( new ByteArrayInputStream(requestEntity) );

        } catch (IOException ex) {
            //Handle logging error
        }
        return b.toString().replaceAll("\\|","||");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("|DEBUG|");
        sb.append(requestContext.getProperty("REQUEST_ID"));
        sb.append("|RESPONSE");
        sb.append("|Header: ").append(responseContext.getHeaders().toString().replaceAll("\\|","||"));
        sb.append("|Entity: [").append(responseContext.getEntity().toString().replaceAll("\\|","||")).append("]|");
        System.out.println(ZonedDateTime.now(ZoneOffset.UTC).format(LOG_FORMAT) + " Z" + sb.toString());
    }
}
