AnSu-IAM
========
This is the description of AnSu-IAM REST API. TODO

**Version:** 1.0.0

**Terms of service:**  


**Contact information:**  
ansu.iam.admin@gmail.com  

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
**Summary:** Update application token life time details TODO

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appId | formData | ID of the application | Yes | string |
| seconds | formData | seconds until which the token will be valid | Yes | integer |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | TODO |

### /user/create
---
##### ***POST***
**Summary:** Create new user

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| name | formData | Name of the user | Yes | string |
| pass | formData | Password to be set | Yes | string |
| appId | formData | Application ID to be used with | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | TODO |

### /user/delete
---
##### ***DELETE***
**Summary:** Delete user associated with appId

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| uid | formData | User ID | Yes | string |
| appId | formData | Application ID to be associated with | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | TODO |

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
| default | TODO |

### /token/generate
---
##### ***POST***
**Summary:** Generate token

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| uid | formData | User's UID | Yes | string |
| pass | formData | Password to be set | Yes | string |
| appId | formData | Application ID to be used with | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | TODO |

### /token/delete
---
##### ***POST***
**Summary:** Remove token from database TODO

**Description:** 

**Parameters**

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| appId | formData | Application ID whose token is to be deleted | Yes | string |
| token | formData | Token to be removed | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| default | TODO |
