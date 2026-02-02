var saPlaceHolder = angular.module('dapp.SaConfigPlaceHolderController',['angularUtils.directives.dirPagination', 'toaster']);

saPlaceHolder.controller('SaConfigPlaceHolderController',['$scope','$window', '$location','$state', '$rootScope','$timeout', 'toaster','FunctionalityService',function($scope, $window, $location,$state, $rootScope,$timeout, toaster, FunctionalityService){
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.allPlaceHolderList;
    $scope.loader= false;
//Expiry certificate type start
    $scope.$on('$viewContentLoaded', function () {    
        FunctionalityService.getExpirycertificateList()
          .then(function (response) {
            if (response.status == 200) {
                console.log("$scope.expirycertificateList::"+JSON.stringify(response));
              $scope.expirycertificateList = response.data.expiryCertificateTypeDTOs;
              
            }
          }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });
      }) 
      $scope.$on('$viewContentLoaded', function () {
      
        $scope.loader= true;

        FunctionalityService.getSubscriptionList($scope.userProfileId)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {

                    $scope.allSubscriptionList = JSON.stringify(response.data.subscriptionInfos);
                    $scope.allSubscriptionList = response.data.subscriptionInfos;
                    console.log("inside placeholder:",JSON.stringify($scope.allSubscriptionList));
                    if ($scope.allSubscriptionList == undefined) {
                        toaster.info({ title: "No records found" });
                    }
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    })
    $scope.organizationChanges = function (organization){
        console.log("second inside",organization);
        $scope.orgName = organization.organizationName;
    }
      $scope.addCertificateType = function (){
        $scope.loader= true;
        var certificateDate = {"certificateType":$scope.certificateType,"expiryHolderDescription":$scope.expirydescription};
        FunctionalityService.addCertificateType(certificateDate)
        .then(function mySuccess(response) {
            $scope.loader= false;
            if (response.status == 201 || response.status == 200) {
                $('#addplaceholder').modal('hide');
                $state.reload();
        $timeout(function () {
            toaster.clear();
            toaster.success({ title: response.data.message });
        }, 1000);

            } else {
                $('#addplaceholder').modal('hide');
                $state.reload();
                toaster.clear();
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });
      }
      $scope.getDeleteCertificate = function() {
        var deleteData = {"certificateTypeId":$scope.editCertificate.certificateTypeId};
        FunctionalityService.deleteCertificateType(deleteData)
        .then(function mySuccess(response) {
            $scope.loader= false;
            if (response.status == 201 || response.status == 200) {
                $('#addplaceholder').modal('hide');
                $state.reload();
        $timeout(function () {
            toaster.clear();
            toaster.success({ title: response.data.message });
        }, 1000);

            } else {
                $('#addplaceholder').modal('hide');
                $state.reload();
                toaster.clear();
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });
      }
      $scope.getEditCertificate = function(data) {
          $scope.editCertificate = data;
      }
      $scope.resetUpdateCertificate = function() {
          var updateData = {"certificateType":$scope.editCertificate.certificateType,"expiryHolderDescription":$scope.editCertificate.expiryHolderDescription,"certificateTypeId":$scope.editCertificate.certificateTypeId};
          FunctionalityService.updateCertificateType(updateData)
          .then(function mySuccess(response) {
              $scope.loader= false;
              if (response.status == 201 || response.status == 200) {
                  $('#addplaceholder').modal('hide');
                  $state.reload();
          $timeout(function () {
              toaster.clear();
              toaster.success({ title: response.data.message });
          }, 1000);
  
              } else {
                  $('#addplaceholder').modal('hide');
                  $state.reload();
                  toaster.clear();
                  toaster.error({ title: response.data.message });
              }
          }, function myError(err) {
              $scope.loader = false;
              console.log("Error response", err);
            });
      }
//Expiry certificate type End
    $scope.expandSubMenu=function(){
        if($rootScope.subMenuActive==true){
            $rootScope.subMenuActive=false;
        }
        else{
            $rootScope.subMenuActive=true;
            $rootScope.subConfigMenuActive=false;
        }

    }

    $scope.expandConfigSubMenu=function(){
        if($rootScope.subConfigMenuActive==true){
            $rootScope.subConfigMenuActive=false;
        }
        else{
            $rootScope.subConfigMenuActive=true;
            $rootScope.subMenuActive=false;
        }

    }


    $scope.placeHolderList = function () {
        $scope.loader= true;

        FunctionalityService.getPlaceHolderList($scope.userProfileId)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {

                    $scope.allPlaceHolderList = JSON.stringify(response.data.documentHolderList);
                    $scope.allPlaceHolderList = response.data.documentHolderList;
                    console.log("$scope.allPlaceHolderList",JSON.stringify($scope.allPlaceHolderList));
                    if ($scope.allPlaceHolderList == undefined) {
                        toaster.clear();
                        toaster.info({ title: "No records found" });
                    }
                }else{
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
        }

        $scope.addPlaceHolder = function () {
            $scope.loader= true;

             var addPlaceHolderData = { "userId": $scope.userProfileId, "documentHolderName": $scope.placeholdername, "documentHolderDescription": $scope.placeholderdescription ,"documentFileNumber": $scope.placeholderfilename,"organizationName": $scope.orgName,"type" : "Standard"};
            //var addPlaceHolderData = { "userId": $scope.userProfileId, "documentHolderName": $scope.placeholdername, "documentHolderDescription": $scope.placeholderdescription ,"documentFileNumber": $scope.placeholderfilename};
            FunctionalityService.addPlaceHolder(addPlaceHolderData)
                .then(function mySuccess(response) {
                    $scope.loader= false;

                    if (response.status == 201 || response.status == 200) {
                        $('#addplaceholder').modal('hide');
                        $state.reload();
                $timeout(function () {
                    toaster.clear();
                    toaster.success({ title: response.data.message });
                }, 1000);
    
                    } else {
                        $('#addplaceholder').modal('hide');
                        $state.reload();
                        toaster.clear();
                        toaster.error({ title: response.data.message });
                    }
                }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                  });
        }

        $scope.clearAddPlaceholder = function (){
            $scope.placeholdername = "";
            $scope.placeholderdescription = "";
            $scope.placeholderfilename = "";
        }

        $scope.updatePlaceHolder;
        $scope.getEditPlaceHolder = function (update) {
            $scope.updatePlaceHolder = update;  
          }
        $scope.editPlaceHolder = function () {
            $scope.loader= true;

            var updatePlaceHolderData = { "userId": $scope.userProfileId, "documentHolderId":$scope.updatePlaceHolder.documentHolderId,"documentHolderName": $scope.updatePlaceHolder.documentHolderName, "documentHolderDescription":$scope.updatePlaceHolder.documentHolderDescription ,"documentFileNumber":$scope.updatePlaceHolder.documentFileNumber,"organizationName":$scope.updatePlaceHolder.organizationName};
            FunctionalityService.editPlaceHolder(updatePlaceHolderData)
                .then(function mySuccess(response) {
                    $scope.loader= false;

                    if (response.status == 201 || response.status == 200) {
                        $('#updateplaceholder').modal('hide');
                        $state.reload();
                $timeout(function () {
                    toaster.clear();
                    toaster.success({ title: response.data.message });
                }, 1000);
    
                    } else {
                        $('#updateplaceholder').modal('hide');
                        $state.reload();
                        toaster.clear();
                        toaster.error({ title: response.data.message });
                    }
                }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                  });
        }

        
        $scope.delete;
        $scope.getDeletePlaceHolder = function (deleteData) {
            $scope.delete = deleteData;  
          }

          $scope.deletePlaceHolder = function () {
            $scope.loader= true;
            
            var deletePlaceHolderData = { "userId": $scope.userProfileId,"documentHolderId":$scope.delete.documentHolderId };
            FunctionalityService.deletePlaceHolder(deletePlaceHolderData)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {
                    $('#delete').modal('hide');
                    $state.reload();
                $timeout(function () {
                    toaster.clear();
                    toaster.success({ title: response.data.message });
                }, 1000);
    
                } else {
                    $('#delete').modal('hide');
                    $state.reload();
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                $('#delete').modal('hide');
                toaster.error({ title:"VesselExit"});
                console.log("Error response", err);
              });
        }

        

        $scope.closeAddplaceholder = function () {
            $('#addplaceholder').modal('hide');
            $state.reload();
          }
          $scope.clsoedUpdateplaceHolder = function () {
            $('#updateplaceholder').modal('hide');
            $state.reload();
          }

          $scope.resetUpdateplaceHolder = function() {
            $scope.updatePlaceholdeForm.$setPristine();
   
            $scope.updatePlaceHolder.documentHolderName="";
           
            $scope.updatePlaceHolder.documentHolderDescription="";
          
            $scope.updatePlaceHolder.documentFileNumber="";
            $scope.certificateType = "";
            $scope.expirydescription = "";
        }; 
        $scope.resetExpiryplaceHolder = function() {            
            $scope.certificateType = "";
            $scope.expirydescription = "";
        }; 
}])
.directive('customFocus', [function () {
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