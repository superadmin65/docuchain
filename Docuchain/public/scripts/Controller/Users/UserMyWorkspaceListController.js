var userMyWorkspaceList = angular.module('dapp.UserMyWorkspaceListController', ['ui.select', 'angularUtils.directives.dirPagination', 'toaster', 'moment-picker', '720kb.tooltips','ngSanitize']);

userMyWorkspaceList.controller('UserMyWorkspaceListController', ['$scope', '$window', '$location', '$state', '$rootScope', '$timeout', 'toaster', '$filter', '$sce', 'FunctionalityService', function ($scope, $window, $location, $state, $rootScope, $timeout, toaster, $filter, $sce, FunctionalityService) {
    $scope.loader = false;
    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

    $scope.groupShipId;
    $scope.groupShipName;

    if($scope.sessionObject.roleId!=3){
   
        $scope.groupShipId=$window.localStorage.getItem('groupShipId');
        $scope.groupShipName=$window.localStorage.getItem('groupShipName');
        $scope.libshipNameDoc =  $scope.groupShipName + " " +"Documents"
    }
    else if ($scope.sessionObject.shipProfileInfos.length>0){
        $scope.groupShipId=$scope.sessionObject.shipProfileInfos[0].id;
        $scope.groupShipName=$scope.sessionObject.shipProfileInfos[0].shipName;
        $scope.libshipNameDoc =  $scope.groupShipName + " " +"Documents"
    }

    $scope.groupList = [];
    $scope.groupListLength = $scope.groupList.length;
    $scope.itemsPerPageGroup = 10;
    $scope.currentPageGroup = 1;
    $scope.loader = false;

    $scope.deleteGroupObj;

    //This method is used to get All Group List
    $scope.getAllGroupList = function () {
        $scope.loader = true;

        var groupdata={"userProfileId":$scope.sessionObject.userId,"shipId":$scope.groupShipId}

        FunctionalityService.getGroupListShip(groupdata)
            .then(function (response) {
                $scope.loader = false;

                if (response.status == 200) {
                    $scope.message = JSON.stringify(response.data.groupList);
                    $scope.groupList = response.data.groupList;
                    $scope.groupListLength = $scope.groupList.length;
                } else {
                    toaster.clear();
                    toaster.error({ title: response.data.message});
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });

            var data1 = {
                "userId": $scope.sessionObject.userId,
                "roleId": $scope.sessionObject.roleId
            };
            FunctionalityService.getVesselProfileList(data1)
                .then(function (response) {
                    $scope.loader = false;
    
                    if (response.status == 200) {
                        $scope.vesselList = response.data.shipProfileList;
                       // $scope.vesselListLength = $scope.vesselList.length;
                    }
                }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                  });
				
    }
    $scope.closeClick = function () {
        $state.reload();
            }
    //This method is used to create Group 
    $scope.createGroupInMyWorkspace = function (group) {
        $scope.loader = true;
        $scope.shipIds=[];
            $scope.shipIds.push($scope.groupShipId);

        var groupData = { 
         "groupName": $scope.group.groupName, 
         "loginId": $scope.sessionObject.userId,
         "userProfileId": $scope.sessionObject.userId, 
         "shipIds": $scope.shipIds, 
         "emailId": $scope.group.emailId,
         "keyword":$scope.group.Keyword 
        };
        FunctionalityService.addGroup(JSON.stringify(groupData))
            .then(function (response) {
                $scope.loader = false;

                if (response.status == 200) {
                    $('#createGroup').modal('hide');
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message});
                        //toaster.pop('success', response.data.message);
                    }, 1000);
                }
                else {
                    $scope.loader = false;
                    toaster.clear();
                    toaster.error({ title: response.data.message});
                    //toaster.pop('error', response.data.message);
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }

    //This method is used to clear the create popup fields
    $scope.clearGroupClearFileds = function () {
        $scope.group.groupName = "";
        $scope.group.emailId = "";
        $scope.group.Keyword=""; 
    }

    //This method is used to store the delete group object
    $scope.storeGroupObjDelete = function (groupData) {
        $scope.deleteGroupObj = groupData;
    }

    //This method is used to delete the group
    $scope.deleteGroup = function () {
        $scope.loader = true;
        var groupData = { 
            "loginId": $scope.sessionObject.userId,
            "groupId":$scope.deleteGroupObj.id
           };
        FunctionalityService.deleteGroup(groupData)
            .then(function (response) {
                $scope.loader = false;

                if (response.status == 200) {
                    $scope.message = response.data;
                    $('#deleteGroup').modal('hide');
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message});
                    }, 20);
                }
                else {
                    $scope.loader = false;
                    toaster.clear();
                    toaster.error({ title: response.data.message});
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }

    //This method is used to share expiry document with windows mail
    $scope.shareExpiryDocumentWithMail=function(group){
        $scope.loader = true;

        $scope.groupExpiryList=[];
        $scope.groupExpiryListLength=$scope.groupExpiryList.length;
        $scope.expDocUrlGroup; 
        FunctionalityService.getAllGroupExpiryDocumentList(group.id)
        .then(function (response) {
            $scope.loader = false;

            $scope.groupExpiryList = response.data.expiryDocumentList;
            $scope.groupEBDListLength = $scope.groupExpiryList.length;

            if( $scope.groupEBDListLength>0){
                angular.forEach($scope.groupExpiryList, function (value, key) {
                    $scope.expDocUrlGroup = $scope.expDocUrlGroup + ("\n" + value.documentHolderName + ": \n" + value.documentDownloadUrl + "\n");
                });
                var mailToLink =group.emailId;
                $scope.url = 'mailto:'  + '?subject='+$scope.groupShipName +'%20Documents'  + '&body=%0D%0A%0D%0ABelow are the list of document attached.%0D%0A%0D%0A' + encodeURIComponent($scope.expDocUrlGroup)+'%0D%0A%0D%0ANote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.'
                $scope.expDocUrlGroupPopup = $scope.expDocUrlGroup + "\n\nNote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled."
                window.location.href = $scope.url;
            }
            
            else{
                toaster.clear();
                toaster.info({ title: "No documents found"});
            }
            console.log("$scope.expDocUrlGroup..",$scope.expDocUrlGroup)
          }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });
        $scope.groupExpiryList=[];
        $scope.expDocUrlGroup="";
    }

}]);
userMyWorkspaceList.directive('customFocus', [function () {
    var FOCUS_CLASS = "custom-focused"; //Toggle a class and style that!
    return {
        restrict: 'A', //Angular will only match the directive against attribute names
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
            ctrl.$focused = false;

            element.bind('focus', function (evt) {
                element.addClass(FOCUS_CLASS);
                scope.$apply(function () { ctrl.$focused = true; });

            }).bind('blur', function (evt) {
                element.removeClass(FOCUS_CLASS);
                scope.$apply(function () { ctrl.$focused = false; });
            });
        }
    }
}]);
userMyWorkspaceList.directive('validateEmail', function() {
    var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
    return {
      link: function(scope, elm) {
        elm.on("keyup",function(){
              var isMatchRegex = EMAIL_REGEXP.test(elm.val());
              if( isMatchRegex&& elm.hasClass('warning-email') || elm.val() == ''){
                elm.removeClass('warning-email');
              }else if(isMatchRegex == false && !elm.hasClass('warning-email')){
                elm.addClass('warning-email');
              }
        });
      }
    }
  });
 //Directive for File Upload
userVesselDocEBD.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);