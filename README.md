AnSu-IAM
========
AnSu-IAM is a IAM (Identity and Authorisation Management) service that can be used for token based authentication.

**Version:** 1.0.0

**Host:** [http://35.154.174.148](http://35.154.174.148)

**BasePath:** /ansu-iam

### /app/
---
##### ***GET***
**Summary:** Get the application's appId

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appName | query | Name of the application | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Check payload for appropriate response |

### /app/create
---
##### ***POST***
**Summary:** Register a new application

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appName | formData | Name of the application to be registered | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Check payload for appropriate response |

### /app/update
---
##### ***PUT***
**Summary:** Update application's token life time (in seconds)

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appId | formData | Application ID (see GET /app/) | Yes | string |
| seconds | formData | seconds for which the token will be valid | Yes | integer |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Status of request |

### /user/create
---
##### ***POST***
**Summary:** Create new user inside the scope of an application (identified by App ID)

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| name | formData | Username | Yes | string |
| pass | formData | User's password | Yes | string |
| appId | formData | Application ID | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Status of request, includes User ID if successful in `payload` field |

### /user/delete
---
##### ***DELETE***
**Summary:** Delete user associated with application (identified by App ID)

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| uid | formData | User ID | Yes | string |
| appId | formData | Application ID | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Status of request |

### /token/checkValid
---
##### ***GET***
**Summary:** Check if token is valid

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appId | query | Application ID | Yes | string |
| token | query | Token whose validity is to be checked | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Status of request, includes `Valid` or `Invalid` message in `payload` field |

### /token/generate
---
##### ***POST***
**Summary:** Generate token

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| uid | formData | User IS | Yes | string |
| pass | formData | User Password | Yes | string |
| appId | formData | Application ID | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Status of request, includes token string in `payload` field |

### /token/delete
---
##### ***POST***
**Summary:** Remove/Invalidate token

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appId | formData | Application ID | Yes | string |
| token | formData | Token to be removed/invalidated | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | Status of request |
