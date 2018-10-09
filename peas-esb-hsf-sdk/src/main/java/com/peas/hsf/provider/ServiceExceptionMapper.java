package com.peas.hsf.provider;

import com.google.gson.Gson;
import com.peas.hsf.exception.CheckerException;
import com.peas.hsf.http.ErrorCode;
import com.peas.hsf.http.ResponseEntity;
import com.peas.hsf.model.Message;
import com.peas.hsf.tool.StreamUtil;
import lombok.extern.log4j.Log4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * 服务异常
 *
 * @author dyh 2015年2月12日
 * @see
 * @since 1.0
 */
@Log4j
public class ServiceExceptionMapper implements ExceptionMapper<Throwable>
{

    private static final Gson gson = new Gson();

    @Override
    public Response toResponse(Throwable throwable)
    {
        ResponseEntity entity = new ResponseEntity();
        if (throwable instanceof CheckerException)
        {
            CheckerException exception = (CheckerException) throwable;
            Message message = exception.getInfo();
            if (!message.isReturnSame())
            {
                if (message.getCode() == ErrorCode.ERROR)
                {
                    entity.setCode(message.getCode());
                    entity.setErrorMessage(message.getMessage());
                }
                else
                {
                    entity.setCode(message.getCode());
                    entity.setMessage(message.getMessage());
                }
            }
            else
            {
                return Response.status(200).encoding("utf-8").entity(message)
                        .type(MediaType.APPLICATION_JSON).build();
            }
        }
        else if (throwable instanceof WebApplicationException)
        {
            WebApplicationException ae = (WebApplicationException) throwable;
            entity.setCode(ae.getResponse().getStatus());
            entity.setErrorMessage(ae.getMessage());
        }
        else
        {
            ByteArrayOutputStream bout = null;
            PrintStream out = null;
            try
            {
                bout = new ByteArrayOutputStream();
                out = new PrintStream(bout);
                throwable.printStackTrace(out);
                String e = new String(bout.toByteArray(), Charset.forName("utf-8"));
                entity.setCode(ErrorCode.ERROR);
                entity.setErrorMessage(e);
            }
            finally
            {
                log.warn("报错了:", throwable);
                StreamUtil.close(out, bout);
            }
        }
        return Response.status(200).encoding("utf-8").entity(gson.toJson(entity))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
