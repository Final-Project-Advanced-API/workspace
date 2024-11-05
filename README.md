# Workspace and User Workspace API

This API allows you to manage workspaces, including creating, updating, and deleting workspaces, as well as managing collaborators within each workspace.

## Table of Contents
- [Workspace Endpoints]
    - [Create Workspace]
    - [Get All Workspaces]
    - [Get Workspace by ID]
    - [Update Workspace]
    - [Delete Workspace]
    - [Update Workspace Status (Private/Public)]
- [User Workspace Endpoints]
    - [Invite Collaborator]
    - [Remove Collaborator]
    - [Get User by User ID and Workspace ID]

## Workspace Endpoints

### Create Workspace

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/workspace-controller/createWorkspace
- **Method**: `POST`
- **Description**: Creates a new workspace and assigns the requesting user as the admin. By default, the workspace is set to private.
- **Request Body**:

    ```json
    {
      "workspaceName": "Spring Boot"
    }
    
    ```

- **Response**:

    ```json
    {
      "message": "Create workspace successfully",
      "payload": {
        "workspaceId": "7badf657-ee42-4b63-b6b7-364a3d931b76",
        "workspaceName": "Spring Boot",
        "isPrivate": true,
        "users": [
          {
            "userId": "b3e623f7-bcd0-4b26-a904-a43ea3ad6d15",
            "username": "mister",
            "fullName": "megamind",
            "gender": "male",
            "dob": "2000-09-31",
            "email": "m.megamind007@gmail.com",
            "profile": "megamind.jpg",
            "bio": "i am megamind",
            "isAdmin": true,
            "createdDate": "2024-10-16T22:44:33.770975700",
            "updatedDate": "2024-10-16T22:44:33.771960800"
          }
        ],
        "createdAt": "2024-11-01T14:19:38.360728",
        "updatedAt": "2024-11-01T14:19:38.360728"
      },
      "status": "CREATED",
      "statusCode": 201,
      "timestamp": "2024-11-01T14:19:38.9570009"
    }
    
    ```


### Get All Workspaces

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/workspace-controller/getAllWorkspace
- **Method**: `GET`
- **Description**: Retrieves a list of all workspaces that the current user is part of.
- **Response**:

    ```json
    {
      "message": "Get all workspace successfully",
      "payload": [
        {
          "workspaceId": "7badf657-ee42-4b63-b6b7-364a3d931b76",
          "workspaceName": "Spring Boot",
          "isPrivate": true,
          "users": [
            {
              "userId": "b3e623f7-bcd0-4b26-a904-a43ea3ad6d15",
              "username": "mister",
              "fullName": "megamind",
              "gender": "male",
              "dob": "2000-09-31",
              "email": "m.megamind007@gmail.com",
              "profile": "megamind.jpg",
              "bio": "i am megamind",
              "isAdmin": true,
              "createdDate": "2024-10-16T22:44:33.770975700",
              "updatedDate": "2024-10-16T22:44:33.771960800"
            }
          ],
          "createdAt": "2024-11-01T14:19:38.360728",
          "updatedAt": "2024-11-01T14:20:38.360728"
        }
      ],
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:19:38.9570009"
    }
    
    ```


### Get Workspace by ID

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/workspace-controller/getWorkspace
- **Method**: `GET`
- **Description**: Retrieves detailed information about a workspace by its ID. Only accessible by the admin or collaborators in the workspace.
- **Path Parameters**:
    - `workspaceId` (required): UUID of the workspace.
- **Response**:

    ```json
    {
      "message": "Get workspace successfully",
      "payload": {
        "workspaceId": "05fbf1dd-da02-4008-be32-7e45173e088d",
        "workspaceName": "Stack Notes",
        "isPrivate": true,
        "users": [
          {
            "userId": "b3e623f7-bcd0-4b26-a904-a43ea3ad6d15",
            "username": "mister",
            "fullName": "megamind",
            "gender": "male",
            "dob": "2000-09-31",
            "email": "m.megamind007@gmail.com",
            "profile": "megamind.jpg",
            "bio": "i am megamind",
            "isAdmin": true,
            "createdDate": "2024-10-16T22:44:33.770975700",
            "updatedDate": "2024-10-16T22:44:33.771960800"
          }
        ],
        "createdAt": "2024-11-01T14:10:48.425",
        "updatedAt": "2024-11-01T14:52:40.970"
      },
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:52:40.9701196"
    }
    
    ```


### Update Workspace

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/workspace-controller/updateWorkspace
- **Method**: `PUT`
- **Description**: Updates the name of an existing workspace. Only the admin of the workspace has permission to update it.
- **Path Parameters**:
    - `workspaceId` (required): UUID of the workspace.
- **Request Body**:

    ```json
    {
      "workspaceName": "Stack Notes"
    }
    
    ```

- **Response**:

    ```json
    {
      "message": "Update workspace successfully",
      "payload": {
    	"workspaceId": "7badf657-ee42-4b63-b6b7-364a3d931b76",
        "workspaceName": "Stack Notes",
        "isPrivate": true,
        "createdAt": "2024-11-01T14:19:38.360728",
        "updatedAt": "2024-11-01T14:19:38.360728"
      },
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:19:38.9570009"
    }
    ```


### Delete Workspace

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/workspace-controller/deleteWorkspace
- **Method**: `DELETE`
- **Description**: Deletes a workspace by its ID. Only the admin of the workspace has permission to delete it.
- **Path Parameters**:
    - `workspaceId` (required): UUID of the workspace.
- **Response**:

    ```json
    {
      "message": "Delete workspace successfully",
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:56:21.596405"
    }
    ```


### Update Workspace Status (Private/Public)

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/workspace-controller/updateStatusWorkspace
- **Method**: `PUT`
- **Description**: Updates the privacy status of a workspace (private or public). Only the admin of the workspace can change this setting.
- **Path Parameters**:
    - `workspaceId` (required): UUID of the workspace.
- **Response**:

    ```json
    {
      "message": "Update status workspace successfully",
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:57:00.2000401"
    }
    
    ```


## User Workspace Endpoints

### Invite Collaborator

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/user-workspace-controller/inviteCollaboratorIntoWorkspace
- **Method**: `POST`
- **Description**: Invites a user to join a workspace as a collaborator. Only the admin of the workspace can invite collaborators.
- **Request Body**:

    ```json
    {
      "email": "collaborator@gmail.com",
      "workspaceId": "7badf657-ee42-4b63-b6b7-364a3d931b76"
    }
    
    ```

- **Response**:

    ```json
    {
      "message": "Collaborator invited to workspace successfully",
      "status": "OK",
      "statusCode": 201,
      "timestamp": "2024-11-01T14:57:00.2000401"
    }
    
    ```


### Remove Collaborator

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/user-workspace-controller/removeCollaboratorFromWorkspace
- **Method**: `DELETE`
- **Description**: Removes a collaborator from a workspace. Only the admin of the workspace has permission to remove collaborators.
- **Request Body**:

    ```json
    {
      "userId": "b3e623f7-bcd0-4b26-a904-a43ea3ad6d15",
      "workspaceId": "7badf657-ee42-4b63-b6b7-364a3d931b76"
    }
    ```

- **Response**:

    ```json
    {
      "message": "Collaborator removed from workspace successfully",
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:57:00.2000401"
    }
    ```


### Get User by User ID and Workspace ID

- **URL**: https://workspace-service.jelay.site/swagger-ui/index.html#/user-workspace-controller/getUserByUserIdAndWorkspaceId
- **Method**:

`GET`

- **Description**: Retrieves information about a specific user in a workspace, including their role. Useful for checking a user's permissions within a workspace.
- **Query Parameters**:
    - `userId` (required): UUID of the user.
    - `workspaceId` (required): UUID of the workspace.
- **Response**:

    ```json
    {
      "message": "User and workspace details retrieved successfully",
      "payload": {
        "userRoleId": "b3e623f7-bcd0-4b26-a904-a43ea3ad6d15",
        "workspaceId": "7badf657-ee42-4b63-b6b7-364a3d931b76",
        "userId": "b3e623f7-bcd0-4b26-a904-a43ea3ad6d15",
        "isAdmin": true
      },
      "status": "OK",
      "statusCode": 200,
      "timestamp": "2024-11-01T14:19:38.9570009"
    }
    
    ```
