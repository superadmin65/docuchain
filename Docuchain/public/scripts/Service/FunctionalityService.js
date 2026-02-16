var serviceApp = angular.module('dapp.FunctionalityService', []);

serviceApp.service('FunctionalityService', [
  '$http',
  '$window',
  '$state',
  '$location',
  function ($http, $window, $state, $location) {
    //var urlBase = 'https://104.215.199.128:8080/dms-dapp-docuchain-api/docuchain/api';
    // var urlBase = 'https://docuchain.sg:8090/dms-dapp-docuchain-api/docuchain/api';
    var urlBase = 'http://localhost:8070/docuchain/api';
    // var urlBase = 'http://192.168.0.31:8070/docuchain/api';

    if (localStorage.length == 0) {
      $location.path('/');
    }
    this.loginSubmit = function (data) {
      return $http.post(urlBase + '/user/login', data);
    };

    this.forgotpassword = function (data) {
      return $http.post(urlBase + '/user/forgotpassword', data);
    };

    this.getVessellist = function (userId) {
      return $http.get(urlBase + '/ship/list/' + userId);
    };

    this.getCountry = function () {
      return $http.get(urlBase + '/common/country/list');
    };

    this.getPort = function (data) {
      return $http.get(urlBase + '/common/country/port/list/' + data);
    };

    this.getShiptype = function (data) {
      return $http.get(urlBase + '/common/vessels/type/list/' + data);
    };

    this.addShip = function (data, shipPic) {
      var formData = new FormData();
      formData.append('shipProfileInfo', JSON.stringify(data));
      formData.append('shipPic', shipPic);
      return $http({
        method: 'POST',
        url: urlBase + '/ship/create',
        headers: { 'Content-Type': undefined },
        data: formData,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };
    // return $http.post(urlBase  '/ship/create', formData);

    //-----------------Admin Ship Profile Started--------------------------------\\

    this.editShip = function (data, shipPic) {
      var formData = new FormData();
      formData.append('shipProfileInfo', JSON.stringify(data));
      formData.append('updateshipPic', shipPic);
      return $http({
        method: 'POST',
        url: urlBase + '/ship/update',
        headers: { 'Content-Type': undefined },
        data: formData,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };

    this.active = function (data) {
      return $http.post(urlBase + '/ship/active/all', data);
    };

    this.deactive = function (data) {
      return $http.post(urlBase + '/ship/deactive/all', data);
    };

    this.delete = function (data) {
      return $http.post(urlBase + '/ship/delete/all', data);
    };

    this.deleteOne = function (data) {
      return $http.post(urlBase + '/ship/delete/', data);
    };

    this.getCommercial = function (data) {
      return $http.get(urlBase + '/user/commercialmanager/list/' + data);
    };

    this.getTech = function (data) {
      return $http.get(urlBase + '/user/techmanager/list/' + data);
    };

    this.getShipmaster = function (data) {
      return $http.get(urlBase + '/user/shipmaster/list/' + data);
    };

    this.getViewShipProfile = function (userId) {
      return $http.get(urlBase + '/ship/viewShipProfile/' + userId);
    };

    this.getDocumentNotificationCount = function (data) {
      return $http.post(urlBase + '/document/notification/count', data);
    };
    this.getTaskStatus = function (data) {
      return $http.get(urlBase + '/task/status/user/' + data);
    };

    this.delShipMaster = function (data) {
      return $http.post(urlBase + '/ship/shipmaster/delete', data);
    };
    this.delTech = function (data) {
      return $http.post(urlBase + '/ship/techmanager/delete', data);
    };
    this.delCom = function (data) {
      return $http.post(urlBase + '/ship/commercialmanager/delete', data);
    };

    this.snoozeUpdate = function (data) {
      return $http.post(urlBase + '/document/notification/snooze/update', data);
    };
    this.getDashboardTopCount = function (userId) {
      return $http.get(urlBase + '/ship/getDashboardTopCount/' + userId);
    };

    this.getDocumentNotification = function (data) {
      return $http.post(urlBase + '/document/notification', data);
    };

    this.getQuestionAndAnswer = function () {
      return $http.get(urlBase + '/common/question/list/all/');
    };

    this.setDocumentNotificationViewed = function (data) {
      return $http.post(urlBase + '/document/notification/viewed', data);
    };

    this.getNotificationByCategory = function (data) {
      console.log(
        'URLBase::' + urlBase + '/document/notification/byCategory',
        data
      );
      return $http.post(urlBase + '/document/notification/byCategory', data);
    };

    this.deleteAllNotification = function (data) {
      console.log(
        'URLBase::' + urlBase + '/document/notification/deleteAll',
        data
      );
      return $http.post(urlBase + '/document/notification/deleteAll', data);
    };

    this.getPendingRequest = function (id) {
      return $http.get(urlBase + '/user/getPendingRequest/' + id);
    };

    this.approvelUser = function (data) {
      return $http.post(urlBase + '/user/approvel', data);
    };
    this.editProfileData = function (data, pic) {
      var formData = new FormData();
      formData.append('updateProfile', JSON.stringify(data));
      formData.append('profilePicture', pic);
      return $http({
        method: 'POST',
        url: urlBase + '/user/profile/update',
        headers: { 'Content-Type': undefined },
        data: formData,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };

    this.resetPswd = function (data) {
      return $http.post(urlBase + '/user/password/update', data);
    };

    //-----------------Admin user Profile Started--------------------------------\\

    this.addUser = function (data, userPic) {
      var formData = new FormData();
      formData.append('userInfo', JSON.stringify(data));
      formData.append('userProfilePicture', userPic);
      return $http({
        method: 'POST',
        url: urlBase + '/user/create',
        headers: { 'Content-Type': undefined },
        data: formData,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };

    this.changeLogo = function (addLogoImage, adminId) {
      var formData = new FormData();
      formData.append('adminId', adminId);
      formData.append('addLogoImage', addLogoImage);
      return $http({
        method: 'POST',
        url: urlBase + '/organization/addLogo',
        headers: { 'Content-Type': undefined },
        data: formData,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };
    this.editUser = function (data, userPic) {
      var formData = new FormData();
      formData.append('userInfo', JSON.stringify(data));
      formData.append('userProfilePicture', userPic);
      return $http({
        method: 'POST',
        url: urlBase + '/user/update',
        headers: { 'Content-Type': undefined },
        data: formData,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };

    this.getOrganizationUserList = function (userId) {
      return $http.get(urlBase + '/user/getorganizationuserlist/' + userId);
    };

    this.getchangeStatusOfUser = function (status) {
      return $http.post(urlBase + '/user/ship/statusUserProfileLists', status);
    };

    this.getVesselsNameList = function (userId) {
      console.log(
        'ibnside the onload functiobn functionality services' +
          urlBase +
          '/common/vessels/type/list/' +
          userId
      );
      return $http.get(urlBase + '/ship/list/organization/' + userId);
    };

    this.getRoleList = function (userId) {
      console.log(
        'ibnside the onload functiobn functionality services' +
          urlBase +
          '/common/roles/list/' +
          userId
      );
      return $http.get(urlBase + '/common/roles/list/' + userId);
    };

    this.activateAllUser = function (status) {
      return $http.post(urlBase + '/user/update/statusall', status);
    };

    this.deActivateAllUser = function (status) {
      return $http.post(urlBase + '/user/update/statusall', status);
    };

    this.getListRolesName = function (userId) {
      return $http.get(urlBase + '/common/roles/list/' + userId);
    };

    this.getListVesselType = function (userId) {
      return $http.get(urlBase + '/common/vessels/type/list/' + userId);
    };

    this.addRolesName = function (data) {
      return $http.post(urlBase + '/common/vessels/type/add', data);
    };

    this.editRole = function (data) {
      return $http.post(urlBase + '/common/roles/update', data);
    };

    this.updateVessels = function (data) {
      return $http.post(urlBase + '/common/vessels/type/update', data);
    };
    this.deleteAdminUser = function (data) {
      return $http.post(urlBase + '/user/delete', data);
    };
    //-----------------Admin user Profile end--------------------------------\\
    //-----------------Super Admin country state Profile Started--------------------------------\\

    this.getCountryList = function () {
      return $http.get(urlBase + '/common/country/list');
    };

    this.addCountry = function (countyData) {
      return $http.post(urlBase + '/common/country/add', countyData);
    };

    this.editCountry = function (countyData) {
      return $http.put(urlBase + '/common/country/update', countyData);
    };

    this.deleteCountry = function (countyData) {
      return $http.post(urlBase + '/common/country/delete', countyData);
    };

    this.getCountryStateList = function () {
      return $http.get(urlBase + '/common/country/port/list/all');
    };

    this.addCountryState = function (countyStateData) {
      return $http.post(urlBase + '/common/country/port/add', countyStateData);
    };

    this.editCountryState = function (countyStateData) {
      return $http.post(
        urlBase + '/common/country/port/update',
        countyStateData
      );
    };
    this.deleteCountryState = function (countyStateData) {
      return $http.post(
        urlBase + '/common/country/port/delete',
        countyStateData
      );
    };

    this.getPlaceHolderList = function () {
      return $http.get(urlBase + '/common/document/holder/list');
    };
    this.getPlaceHolderListByorganizatinId = function (data) {
      return $http.post(
        urlBase + '/common/document/holder/list/organization',
        data
      );
    };
    this.getExpirycertificateList = function () {
      return $http.get(
        urlBase + '/common/expiry/document/certificateType/list'
      );
    };
    this.addCertificateType = function (data) {
      return $http.post(
        urlBase + '/common/expiry/document/certificateType/add',
        data
      );
    };
    this.updateCertificateType = function (data) {
      return $http.post(
        urlBase + '/common/expiry/document/certificateType/update',
        data
      );
    };
    this.deleteCertificateType = function (data) {
      return $http.post(
        urlBase + '/common/expiry/document/certificateType/delete',
        data
      );
    };
    this.addPlaceHolder = function (countyStateData) {
      return $http.post(
        urlBase + '/common/document/holder/add',
        countyStateData
      );
    };
    this.editPlaceHolder = function (countyStateData) {
      return $http.put(
        urlBase + '/common/document/holder/update',
        countyStateData
      );
    };
    this.deletePlaceHolder = function (data) {
      return $http.delete(
        urlBase +
          '/common/document/holder/delete/' +
          data.userId +
          '/' +
          data.documentHolderId
      );
    };

    this.getOrganizationTopCount = function () {
      return $http.get(urlBase + '/organization/top/count');
    };
    this.getStatisticsDetail = function () {
      return $http.get(urlBase + '/organization/statistics/detail');
    };
    //-----------------Super Admin country state Profile end--------------------------------\\
    //-----------------User API call Service Starts--------------------------------\\

    this.getShipProfileList = function (userId) {
      return $http.get(urlBase + '/ship/list/organization/' + userId);
    };

    this.getVesselProfileList = function (data) {
      return $http.post(urlBase + '/user/ship/get', data);
    };

    this.getAllExpiryDocumentList = function (shipId, archivedStatus) {
      console.log(
        "urlBase + '/expiry/document/holder/all/' + shipId + " /
          ' + archivedStatus' +
          urlBase +
          '/expiry/document/holder/all/' +
          shipId +
          '/' +
          archivedStatus
      );
      return $http.get(
        urlBase + '/expiry/document/holder/all/' + shipId + '/' + archivedStatus
      );
    };

    this.getShipList = function (userId) {
      return $http.get(urlBase + '/ship/list/organization/' + userId);
    };

    this.getDashboardTopCountBasedOnVessel = function (vesselId) {
      return $http.get(
        urlBase + '/ship/getDashboardTopCountBasedOnVessel/' + vesselId
      );
    };
    this.scanExpiryDocument = function (file) {
      var formdata = new FormData();
      formdata.append('scanFile', file);
      return $http({
        method: 'POST',
        url: urlBase + '/expiry/document/scan',
        headers: { 'Content-Type': undefined },
        data: formdata,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };

    this.saveExpiryDocument = function (saveData, file) {
      var formdata = new FormData();
      formdata.append('ExpiryDocumentInfo', saveData);
      formdata.append('scanFile', file);
      return $http({
        method: 'POST',
        url: urlBase + '/expiry/document/add',
        headers: { 'Content-Type': undefined },
        data: formdata,
        transformRequest: function (data, headersGetter) {
          return data;
        },
      });
    };

    this.updateExpiryDocument = function (data) {
      return $http.post(urlBase + '/expiry/document/update', data);
    };
    this.getDoucmentApprovalListService = function (data) {
      return $http.post(urlBase + '/expiry/document/getExpDocumentInfo', data);
    };

    this.chageExpiryDocumentStatus = function (data) {
      return $http.post(urlBase + '/document/approval', data);
    };

    this.getDocumentHolderHistory = function (data) {
      return $http.post(urlBase + '/expiry/document/histroy', data);
    };

    this.getGroupList = function (userId) {
      return $http.get(urlBase + '/group/all/' + userId);
    };

    this.addGroup = function (data) {
      return $http.post(urlBase + '/group/create', data);
    };

    this.getGroupListShip = function (data) {
      return $http.post(urlBase + '/group/list', data);
    };

    this.deleteGroup = function (groupId) {
      return $http.get(urlBase + '/group/delete/' + groupId);
    };

    this.getAllGroupExpiryDocumentList = function (groupId) {
      return $http.get(urlBase + '/group/expirydocument/' + groupId);
    };

    this.deleteGroup = function (data) {
      return $http.post(urlBase + '/group/delete', data);
    };

    this.addExpiryDocToGroup = function (addExpiryDocToGroupData) {
      return $http.post(
        urlBase + '/group/add/exiprydocument',
        addExpiryDocToGroupData
      );
    };

    this.deleteGroupExpiryDocument = function (groupExpiryDocumentData) {
      return $http.post(
        urlBase + '/group/delete/exiprydocument',
        groupExpiryDocumentData
      );
    };

    this.viewGroup = function (groupId) {
      return $http.get(urlBase + '/group/view/' + groupId);
    };

    this.shareExpiryDoc = function (groupData) {
      return $http.post(urlBase + '/group/add/exp', groupData);
    };

    this.updateshareExpiryDoc = function (exp) {
      return $http.post(urlBase + '/group/update/exp', exp);
    };
    // this.getDocumentNOtification = function (data) {
    //     return $http.post(urlBase + '/document/notification', data);
    // };
    this.addGeoLocation = function (data) {
      return $http.post(urlBase + '/user/addGeoLocation', data);
    };
    this.geoLocationlist = function (userId) {
      return $http.get(urlBase + '/user/get/geoLocationlist/' + userId);
    };
    //-----------------User API call Service end--------------------------------\\

    // Task manager service
    this.getTaskAssignedByUser = function (userProfileId) {
      return $http.get(urlBase + '/task/assignedbyuser/' + userProfileId);
    };
    this.getTaskAssignedToUser = function (userProfileId) {
      return $http.get(urlBase + '/task/assignedtouser/' + userProfileId);
    };
    //updateShipRelatedTask
    this.updateShipRelatedTask = function (data) {
      return $http.post(urlBase + '/task/update/shiprelatedtask', data);
    };

    this.getUserProfileList = function (userId) {
      console.log('url service::::', urlBase + '/user/list/all/' + userId);
      return $http.get(urlBase + '/user/list/all/' + userId);
    };

    this.createShipRelatedTask = function (data) {
      return $http.post(urlBase + '/task/create/shiprelated', data);
    };
    this.getuserList = function (userId, url) {
      console.log('before get userLisat::' + urlBase + url + userId);
      return $http.get(urlBase + url + userId);
    };
    this.addRequestUser = function (data) {
      console.log(
        'before services request user:::' + urlBase + '/user/request',
        data
      );
      return $http.post(urlBase + '/user/request', data);
    };

    this.getTaskStatusFromAssignedUser = function (taskId) {
      return $http.get(urlBase + '/task/status/user/' + taskId);
    };

    this.deleteTask = function (taskId) {
      return $http.post(urlBase + '/task/delete', taskId);
    };
    this.taskStatusList = function () {
      return $http.get(urlBase + '/task/status/all');
    };
    this.updateStatusWithRemarks = function (taskInfo) {
      console.log(urlBase + '/task/status/update');
      return $http.post(urlBase + '/task/status/update', taskInfo);
    };
    //-----task management end---------------//

    //-----------------Admin AauditLog  Started--------------------------------\\
    this.getHistorybyadmin = function (data) {
      return $http.post(urlBase + '/history/getHistorybyadmin', data);
    };
    this.gethistorybasedonselecttype = function (data) {
      return $http.post(urlBase + '/history/gethistorybasedonselecttype', data);
    };

    //-----------------Admin AauditLog End--------------------------------\\

    //------------------------------------create organisation-----------------//

    this.createOrganization = function (data) {
      return $http.post(urlBase + '/organization/create', data);
    };

    this.createOrganizationwithSubcription = function (data) {
      return $http.post(
        urlBase + '/organization/create/with/subscription',
        data
      );
    };
    this.getOrganizationList = function (data) {
      return $http.get(urlBase + '/organization/list/' + data);
    };
    this.changeStatusOrganization = function (data) {
      return $http.post(urlBase + '/organization/active', data);
    };
    this.changeOrgDualApprovel = function (data) {
      return $http.post(urlBase + '/organization/dual/approval', data);
    };

    this.changeSaveInBlockchain = function (data) {
      return $http.post(urlBase + '/organization/blockchain/active', data);
    };

    this.deleteOrganization = function (data) {
      return $http.delete(
        urlBase +
          '/organization/delete/' +
          data.userId +
          '/' +
          data.organizationId
      );
    };

    this.getorganizationView = function (data) {
      return $http.get(urlBase + '/organization/view/' + data);
    };
    this.updatOrganization = function (data) {
      return $http.put(urlBase + '/organization/update', data);
    };
    this.getSubscriptionList = function () {
      return $http.get(urlBase + '/subscription/list');
    };
    this.subscriptionAdminList = function (data) {
      return $http.get(urlBase + '/organization/list/admin/' + data);
    };
    this.addSubAdmin = function (data) {
      return $http.post(urlBase + '/organization/save/admin', data);
    };
    // this.updateAdmin = function (data) {
    //     return $http.post(urlBase + '/user/admin/update', data);
    // };
    this.deleteAdmin = function (data) {
      //return $http.post(urlBase + '/user/admin/update', data);
      return $http.delete(
        urlBase + '/user/admin/delete/' + data.superAdminId + '/' + data.adminId
      );
    };
    this.editSubscription = function (data) {
      return $http.post(urlBase + '/organization/subscription/update', data);
    };

    this.editAndUpdateSubscription = function (data) {
      return $http.post(urlBase + '/subscription/plan/update', data);
    };

    this.resetPasswordAdmin = function (data) {
      return $http.post(urlBase + '/user/admin/password/reset', data);
    };

    //------------------------------------end organisation-----------------//
    //-----------------Super Admin AauditLog  Started--------------------------------\\
    this.getHistorybysuperAdmin = function (data) {
      return $http.post(urlBase + '/history/getHistorybySuperadmin', data);
    };
    this.gethistorybasedSuperAdminonselecttype = function (data) {
      return $http.post(urlBase + '/history/getbyselecttype', data);
    };
    //-----------------Super Admin AauditLog End--------------------------------\\
    this.reportanIssue = function (data) {
      return $http.post(urlBase + '/common/report/an/issue', data);
    };
    //----------------Delete Notification Start-----------------------\\
    this.deleteNotification = function (data) {
      return $http.post(urlBase + '/document/notification/delete/', data);
    };
    this.deleteAllNotification = function (data) {
      console.log(
        'URLBase::' + urlBase + '/document/notification/deleteAll',
        data
      );
      return $http.post(urlBase + '/document/notification/deleteAll', data);
    };
    //---------------------End-----------------------------------------\\
  },
]);
