<div align="center">

# 赛道友你聊天服务

<!-- markdownlint-disable-next-line MD036 -->
_✨ Author: [Nagico](https://github.com/Nagico/) ✨_
</div>

<p align="center">
  <img src="https://img.shields.io/badge/LICENSE-AGPLv3-red" alt="license">
  <a href="stargazers">
    <img src="https://img.shields.io/github/stars/Nagico/teamup_chat?color=yellow&label=Github%20Stars" alt="star">
  </a>
  <br />
  <img src="https://img.shields.io/badge/Java-17-red" alt="java">
  <img src="https://img.shields.io/badge/Kotlin-1.7-purple" alt="kotlin">
  <img src="https://img.shields.io/badge/Springboot-3.0.5-greeaen" alt="springboot">
  <br />
  <a href="https://github.com/Nagico/teamup_chat">
    <img src="https://img.shields.io/badge/Github-general-brightgreen?logo=github" alt="chat_repository">
  </a>
  <a href="https://github.com/Nagico/teamup_backend/">
    <img src="https://img.shields.io/badge/Github-backend-brightgreen?logo=github" alt="backend_repository">
  </a>
  <br />
</p>
<!-- markdownlint-enable MD033 -->

## 项目地址

- 测试环境: wss://chat.test.teamup.nagico.cn/


## 技术栈

- Springboot
- MyBatis
- Netty

## 使用说明

### 文档版本

|   版本   |    日期     |   作者   |                                                                                               备注                                                                                                |
|:------:|:---------:|:------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| v0.0.1 | 2023-5-10 | Nagico |                                                                                               初稿                                                                                                |
| v0.0.2 | 2023-6-4  | Nagico |  增加DEBUG CONNECT，[More](https://github.com/Nagico/teamup_chat/commit/8f762d4ea9d79d92437b5fb769ecbd97c6034459#diff-b335630551682c19a781afebcf4d07bf978fb1f8ac04c6bf87428ed5106870f5R193-R226)   |
| v0.1.0 | 2023-6-4  | Nagico | 支持SUBSCRIBE，取消自动订阅，[More](https://github.com/Nagico/teamup_chat/commit/098ed54a532d63f1ea5306a484f92346a343d4ef#diff-b335630551682c19a781afebcf4d07bf978fb1f8ac04c6bf87428ed5106870f5R213-R263) |
| v0.1.1 | 2023-6-4  | Nagico |                                                                                     MESSAGE添加destination头部                                                                                      |

### 规定

聊天模块使用Websocket作为通信协议，通信数据依据STOMP协议进行修改。消息采用JSON格式，须遵循以下格式

```json5
{
    "type": 1,  // 消息类型 请参考下方
    "content": "hello world",  // 消息内容 str
}
```

详细内容请参考 [STOMP v1.2](https://stomp.github.io/stomp-specification-1.2.html)

#### 术语

- 服务器(server): websocket 聊天服务器
- 客户端(client): 准备接入聊天服务的用户端
- 用户(user): teamup_backend 内的用户
- 登录密钥(token): teamup_backend 用户登录后获得的 access token
- 会话(session): client 与 server 连接的通道，每次连接都具有唯一性
- 帧(frame): 满足 STOMP 协议通过 Websocket 传输的有效数据
- 命令(帧类型, command): stomp frame 第一行，表明当前帧的类型，使用全大写，在文档中一般表示为 ${COMMAND} frame/帧
- 头部(header): stomp frame 第二行到 body 之间的内容，格式为 ${KEY}:${VALUE}，用 `\n` 分隔
- 内容(body): stomp frame 头部后的内容，用 `单独的空行` 与 headers 分隔，最后以 `\0` 结尾
- 消息(message): SEND 帧 的 body，一般为 `application/json` 格式
- 消息 id(message id): 消息的唯一标识符，为 uuid4 格式，建议带上 `-`
- 目的地址(地址, destination): 消息送达的目的地，client接受消息的地址
- 订阅(subscribe): 用户在当前会话下进行目的地的订阅，用于接受发送到该地址下的消息
- 消息类型(message type): 消息的type字段
- 消息内容(message content): 消息的content字段

#### 帧类型

- CONNECT 帧(client -> server): client 主动建立 stomp 协议连接
- CONNECTED 帧(server -> client): server确定连接建立成功后返回的基本信息
- MESSAGE 帧(server -> client): server 发送的消息，其来源于向订阅的目的地址发送的消息
- ACK 帧(client -> server): `client 确认`, client 接收到 MESSAGE 后向 server 确认已收到
- SEND 帧(client -> server): client 向某个目的地址发送消息
- RECEIPT 帧(server -> client): `server 确认`, 当帧中包含 receipt 头部时，server 正确处理该消息后向 client 返回 RECEIPT 帧
- DISCONNECT 帧(client -> server): `client 断开连接`, 客户端主动断开连接，表示会话结束
- ERROR 帧(server -> client): `server 断开连接`, 当发生异常时，服务端汇报错误并主动断开连接

#### 消息类型

- CHAT 1: 普通聊天消息, content 为具体的内容
- READ 2: 已读回执, content 为所读的 message id

#### 符号

- NULL: NULL表示结束符 `\0`
- ${}: 括号内为变量的值
- []: 表示括号内的属性为可选项

#### 特殊处理

由于本应用聊天功能只支持私聊功能，故对 STOMP 协议做出以下修改

- 用户连接成功在 CONNECTED 帧后服务器会自动订阅 `destionation为user_id` 的地址，并且按照时间顺序依次返回 `未 ACK` 的 MESSAGE 帧
- 不支持 `UNSUBSCRIBE`, `NACK`, `BEGIN`, `COMMIT`, `ABORT` 帧

### 登入

用户连接上ws服务后，凭借 access token进行登录

**发送 CONNECT 帧**

```http request
CONNECT
accept-version:1.2,1.1,1.0
heart-beat:0,0
Authentication:Bearer ${token}

NULL
```

- accept-version: 客户端所支持的 stomp 协议版本
- heart-beat: 客户端所期望的心跳包发送模式 此处为停用心跳包
- Authentication: token 认证

#### 成功

**接收 CONNECTED 帧**

```http request
CONNECTED
version:1.2
session:0242acfffe120006-00000001-0000004b-a99acb566ce1955b-055ec830
server:Netty/4.1 (4e4194fa-16ea-490a-ad4c-940cff3b7a9d)
heart-beat:0,0
user:3

NULL
```

- version: 协商后采用的 stomp 版本 服务器支持 `STOMP-v1.1` 与 `STOMP-v1.2`
- session: 当前会话的 id
- server: 服务器类型/服务器版本 (服务器 id)
- heat-beat: 协商后采用的心跳包模式 服务端强制禁用心跳包
- user: 当前登录的用户 id

~~此时服务器会自动订阅 `{user_id}` 的目的地址，用于用户接受私人消息~~

v0.1.0 版本后，服务器不再自动订阅，而是在用户主动订阅后，根据时间顺序依次返回 `未 ACK` 的 MESSAGE 帧

#### 失败

- 未提交 token


**接收 ERROR 帧**

```http request
ERROR
message:header_missing

Required 'Authentication' header missedNULL
```

- token 错误

**接收 ERROR 帧**

```http request
ERROR
message:unknown_error

Invalid serialized unsecured/JWS/JWE object: Missing part delimitersNULL
```

### USERID登入 (DEBUG ONLY)

用户连接上ws服务后，凭借 user_id 进行登录，用于调试

**发送 CONNECT 帧**

```http request
CONNECT
accept-version:1.2,1.1,1.0
heart-beat:0,0
UserId:${user_id}

NULL
```

- accept-version: 客户端所支持的 stomp 协议版本
- heart-beat: 客户端所期望的心跳包发送模式 此处为停用心跳包
- user_id: 预期登录的用户ID，需存在

#### 成功

**接收 CONNECTED 帧**

```http request
CONNECTED
version:1.2
session:0242acfffe120006-00000001-0000004b-a99acb566ce1955b-055ec830
server:Netty/4.1 (4e4194fa-16ea-490a-ad4c-940cff3b7a9d)
heart-beat:0,0
user:2

NULL
```

### 订阅地址

v0.1.0版本后，用户需要手动${user_id}订阅地址，用于接收私人消息

**发送SUBSCRIBE帧**
    
```http request
SUBSCRIBE
id:${id}
destination:${user_id}
ack:client-individual

NULL
```

- id: 订阅id，~~用于取消订阅~~，暂无用处
- user_id: 用户id，需与已登陆id一致
- ack: 客户端确认模式，此处必须为 `client-individual`，即每个MESSAGE帧都需要独立的ACK

#### 成功

若用户存在未 ACK 的消息，此时服务器根据时间顺序依次发送消息原文

**接收若干 MESSAGE 帧**

```http request
MESSAGE
id:344465d0-8914-41cf-bea5-449b6ced5184
content-type:application/json
content-length:27

{"content":"1234","type":1}NULL
```

- id: message id

注意，确认消息接收后需要对消息进行 ACK 返回

#### 失败

- 订阅地址错误

**接收 ERROR 帧**

```http request
ERROR
message:permission_error

user 1 cannot subscribe to 2NULL
```

### 发送消息

**发送 SEND 帧**

```http request
SEND
id:${message-id}
destination:${receiver}
content-type:application/json
content-length:${len(msg)}
[receipt: ${receipt-id}]

${msg}NULL
```
- message-id: 为生成的message id，用于判断消息唯一性
- receiver: 表示接收方的用户 id
- receipt-id: 随机字符串，建议random int (0, 10000)，服务器返回 RECEIPT 帧时会将该内容填入 receipt-id header 内
- msg: 消息内容，需遵循 `{type:1,content:""}` 的 JSON 数据格式

若指定了 receipt header，服务器正确处理消息后将会返回 RECEIPT 帧

**接收 RECEIPT 帧**

```http request
RECEIPT
receipt-id: ${receipt-id}

NULL
```

- receipt-id: 表示该帧是服务器对哪个帧进行的确认，该值与 receipt header 的值一致

### 接收消息

**接收 MESSAGE 帧**

```http request
MESSAGE
id:${message-id}
sender:${sender}
destination:${receiver}
create-time:${create-time}
content-type:application/json
content-length: ${len(msg)}

${msg}NULL
```

- message-id: 消息 id
- sender: 发送方用户 id
- receiver: 接收方用户 id
- create-time: 消息创建时间

当客户端确认消息被正确接收后，需要向服务器返回 ACK 帧，否则该消息会被一直标记为未接收状态，并在用户下一次 CONNECT 后返回，直到被 ACK

**发送 ACK 帧**

```http request
ACK
message-id:${message-id}

NULL
```

- message-id: 确认接收成功的message id

该帧不支持 receipt header

### 断开连接

**发送 DISCONNECT 帧**

```http request
DISCONNECT
receipt:${receipt-id}

NULL
```

该帧强制需要 receipt header，避免客户端单方面断开连接而服务端仍有消息需要传递

**接收 RECEIPT 帧**

```http request
RECEIPT
receipt-id:${receipt-id}

NULL
```

此时客户端可以安全断开连接

### 完整示例

接下来以 user 3 为例，展示完整通信流程

1. 建立连接

- 发送

```http request
CONNECT
accept-version:1.2,1.1,1.0
heart-beat:0,0
Authentication:Bearer --secret--

NULL
```

- 接收

```http request
CONNECTED
version:1.2
session:0242acfffe120006-00000001-00000090-95609f08f9cf537c-606cead6
server:Netty/4.1 (198c8700-3662-4012-bfff-a1d32e2973be)
heart-beat:0,0
user:3

NULL
```

```http request
MESSAGE
id:dd618415-ff2b-4226-8ec5-738302cf7272
content-type:application/json
content-length:48

{"content":"first message from user 2","type":1}NULL
```

```http request
MESSAGE
id:c7357ed8-f07b-4db2-b94c-76e28e13c8fe
content-type:application/json
content-length:43

{"content":"first message from 4","type":1}NULL
```

```http request
MESSAGE
id:c8ebb5ee-df1d-4ff3-b741-430d134687d6
content-type:application/json
content-length:44

{"content":"second message from 2","type":1}NULL
```

2. 确认未 ACK 消息

- 发送

```http request
ACK
message-id:dd618415-ff2b-4226-8ec5-738302cf7272

NULL
```

```http request
ACK
message-id:c7357ed8-f07b-4db2-b94c-76e28e13c8fe

NULL
```

```http request
ACK
message-id:c8ebb5ee-df1d-4ff3-b741-430d134687d6

NULL
```

3. 发送新消息

- 发送

```http request
SEND
id:1d2362bc-c9c7-4427-8989-f6d4d655e428
destination:2
content-type:application/json
receipt: 1990
content-length:62

{"type": 2, "content": "c8ebb5ee-df1d-4ff3-b741-430d134687d6"}NULL
```

- 接收

```http request
RECEIPT
receipt-id: 1990

NULL
```

- 发送

```http request
SEND
id:b31f065c-d91f-48e9-92cc-642142a79a9f
destination:2
content-type:application/json
receipt: 3979
content-length:53

{"type": 1, "content": "receive your second message"}NULL
```

```http request
RECEIPT
receipt-id: 3979

NULL
```

4. 断开连接

- 发送

```http request
DISCONNECT
receipt:26026

NULL
```

- 接收

```http request
RECEIPT
receipt-id:26026

NULL
```