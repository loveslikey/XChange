package org.knowm.xchange.huobi;

import org.knowm.xchange.huobi.dto.ApiResponse;
import org.knowm.xchange.huobi.dto.account.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Administrator on 2018/2/20.
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface HuobiAuthenticated extends Huobi {

    @GET
    @Path("v1/account/accounts")
    ApiResponse<List<Account>> accounts(@QueryParam("id") Long id, @QueryParam("state") String state, @QueryParam("type") String type) throws ApiException;


}
