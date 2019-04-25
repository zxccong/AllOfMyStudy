local cjson = require("cjson") 
local redis = require("resty.redis")  
local http = require("resty.http")

local function close_redis(red)  
	if not red then  
		return  
	end  
	local pool_max_idle_time = 10000 
	local pool_size = 100 
	local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)  
	if not ok then  
		ngx.say("set keepalive error : ", err)  
	end  
end  
  


local uri_args = ngx.req.get_uri_args()

local productId = uri_args["productId"]

local cache_ngx = ngx.shared.my_cache


local productCacheKey = "product_"..productId

local productCache = cache_ngx:get(productCacheKey)

if productCache == "" or productCache == nil then
	
	local red = redis:new()  
	red:set_timeout(1000)  
	local ip = "192.168.222.135"  
	local port = 1112  
	local ok, err = red:connect(ip, port)  
	if not ok then  
		ngx.say("connect to redis error : ", err)  
		return close_redis(red)  
	end  
	  
	local resp, err = red:get("dim_product_"..productId)  
	if not resp then  
		ngx.say("get msg error : ", err)  
		return close_redis(red)  
	end  
	
	if resp == ngx.null then  
		
		local httpc = http.new()

		local resp, err = httpc:request_uri("http://10.10.86.6:8767",{
			method = "GET",
			path = "/product?productId="..productId,
			keepalive=false
		})

		productCache = resp.body
		
		
		
	end  

	ngx.say("msg : ", resp)  
	  
	close_redis(red)

	math.randomseed(tostring(os.time()):reverse():sub(1,7))
	local expireTime = math.random(600,1200)
		
	cache_ngx:set(productCacheKey, productCache, expireTime)
	
	
end

local productCacheJSON = cjson.decode(productCache)




local context = {
	productInfo = productCache
	
}

local template = require("resty.template")
template.render("product.html", context)
