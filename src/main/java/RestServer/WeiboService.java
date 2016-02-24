/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package RestServer;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import Database.JDBC;
import Log.Log;
import Weiobo.weibo.query.MysqlWritingPool;

@Path("/")
public class WeiboService {

	@POST
	@Path("/unread/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response addUnread(@FormParam("unread") String unreadStr, @FormParam("contributer") String contributer) {
		Log.write("log.txt","Contributer: "+contributer);
		MysqlWritingPool thread = new MysqlWritingPool("Thread 1");
		thread.generateUnreadSql(new JSONArray(unreadStr), contributer);
		// Thread mysqlWrite=new Thread(thread);
		//System.out.println("Arrive at 46:");
		thread.run();
		return Response.ok().build();
	}

	@POST
	@Path("/update/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response update(@FormParam("user") String userStr) {
		MysqlWritingPool thread = new MysqlWritingPool("Thread 1");
		thread.generateUpdateSql(new JSONObject(userStr));
		// Thread mysqlWrite=new Thread(thread);
		thread.run();
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/nextsql/")
	public Response getCustomer() {
		// System.out.println("----invoking getCustomer, Customer id is: ");
		return Response.ok(JDBC.getRandomId()).build();
	}
}
