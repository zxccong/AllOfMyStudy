local uri_args = ngx.req.get_uri_args()
local productId = uri_args["productId"]
local shopId=uri_args["shopId"]

local host = {"192.168.222.131", "192.168.222.132"}
local backend = ""

local hot_product_flag = "hot_product_"..productId

local cache_ngx = ngx.shared.my_cache
local hot_product_flag = cache_ngx:get(hot_product_flag)

if hot_product_flag == "true" then
    math.randomseed(tostring(os.time()):reverse():sub(1,7))
    local index = math.random(1,2)
    backend = "http://"..host[index]
else
    local hash = ngx.crc32_long(productId)
    hash = (hash % 2) + 1  
    backend = "http://"..host[hash]
end

local method = uri_args["method"]
local requestBody = "/"..method.."?productId="..productId.."&shopId="..shopId

local http = require("resty.http")  
local httpc = http.new()  

local resp, err = httpc:request_uri(backend, {  
    method = "GET",  
    path = requestBody,
    keepalive=false
})

if not resp then  
    ngx.say("request error :", err)  
    return  
end

ngx.say(resp.body)  
  
httpc:close() 
