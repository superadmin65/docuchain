var adminVesselsadd = angular.module('dapp.AdminVesselsAddinfoController', ['ui.select', 'ngSanitize']);
adminVesselsadd.directive("fileInput", ['$parse', function ($parse) {
  return {
    restrict: 'A',
    link: function (scope, ele, attrs) {
      ele.bind('change', function () {
        $parse(attrs.fileInput).
          assign(scope, ele[0].files)
        scope.$apply()
      });
    }
  }
}]);

adminVesselsadd.controller('AdminVesselsAddinfoController', ['$scope', '$timeout', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', '$http', 'toaster', function ($scope, $timeout, $window, $location, $state, $rootScope, FunctionalityService, $http, toaster) {

  $scope.userProfileId = $window.localStorage.getItem('userId');
  $scope.userId = $window.localStorage.getItem('userId');
  $scope.organizationId = $window.localStorage.getItem('organizationId');
  $scope.organizationName = $window.localStorage.getItem('organizationName');
  $scope.adminUserList;
  $scope.selctedUserList = [];
  $scope.example1model = [];
  $scope.example2settings = { displayProp: 'id' };
  $scope.popuptitle = "Add New User";
  $scope.myFile = "";
  $scope.file;
  $scope.user = {};
  $scope.password = true;
  $scope.confirmPassword = true;
  $scope.roleCheckmultiselece = true;
  $scope.roleCheck = false;
  $scope.updateUNameEnable = false;
  $scope.updateUNameDisable = true;
  $scope.currentPage = 1;
  $scope.viewby = 5;
  $scope.uiselectshow = true;
  $scope.signleVesselName = false;
  $scope.itemsPerPage = $scope.viewby;
  $scope.addPopupsubmit = true;
  $scope.editPopupsubmit = false;
  $scope.roleList = {};
  $scope.userData = {};
  $scope.countryInfos = [];
  $scope.countryId = [];
  $scope.portInfos = [];
  $scope.countryName;
  $scope.vesselUser = false;
  $scope.vesselTick = false;
  $scope.vesselsTypeInfos = [];
  $scope.shipPic;
  $scope.commercialManagerInfos = [];
  $scope.technicalManagerInfos = [];
  $scope.shipMasterInfos = [];
  $scope.commercialManagerInfoList = [];
  $scope.techManagerInfoList = [];
  $scope.shipUserId;
  $scope.techManagerIds = [];
  $scope.commercialMasterIds = [];
  $scope.allPlaceHolderListCustom = [];
  $scope.selectedTechdetails;
  $scope.selectedComdetails;
  $scope.shipMasterName = "Ship Master";
  $scope.technicalmasterName = "Technical";
  $scope.commercialmasterName = "Commercial";
  $scope.roleAliasList;
  $scope.roleId;
  $scope.loader = false;
  $scope.vesselModel = true;
  callCommercial();
  callTech();
  callShipmaster();

  $scope.$on('$viewContentLoaded', function () {
    FunctionalityService.getListRolesName($scope.userProfileId)
      .then(function (response) {
        if (response.status == 200) {
          $scope.roleAliasList = response.data.roleAliasInfos;
          angular.forEach($scope.roleAliasList, function (obj) {
            if (obj.roleId == 3) {
              $scope.shipMasterName = obj.roleAliasName;
            } else if (obj.roleId == 4) {
              $scope.technicalmasterName = obj.roleAliasName;
            } else if (obj.roleId == 5) {
              $scope.commercialmasterName = obj.roleAliasName;
            }
          })
        }
      }, function (error) {
        console.log("message :: " + error);
      });
  })
  $scope.$on('$viewContentLoaded', function () {
    FunctionalityService.getVesselsNameList($scope.userProfileId)
      .then(function (response) {
        if (response.status == 200) {
          $scope.users = response.data.shipProfileList;
        }
      }, function (error) {
        console.log("message :: " + error);
      });
  })

  $scope.$on('$viewContentLoaded', function () {
    FunctionalityService.getRoleList($scope.userProfileId)
      .then(function (response) {
        if (response.status == 200) {
          $scope.roleList = response.data.roleAliasInfos;
        }
      }, function (error) {
        console.log("message :: " + error);
      });
  })

  $scope.selectedUsers = [{}];

  $scope.auth = {}

  $scope.thumbnail = {
    // dataUrl: 'adsfas'
  };
  $scope.fileReaderSupported = window.FileReader != null;
  $scope.uploadFile = function (files) {
    if (files != null) {
      var file = files[0];

      if (files[0].size > 2048000) {
        $rootScope.errorFile = document.getElementById('sizeOffile').innerHTML = "Picture should be below 1MB";
      }
      else {
        document.getElementById('sizeOffile').innerHTML = "";
        $rootScope.errorFile = "";
      }

      $scope.shipPic = file;

      if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
        $timeout(function () {
          var fileReader = new FileReader();
          fileReader.readAsDataURL(file);
          fileReader.onload = function (e) {
            $timeout(function () {
              $scope.thumbnail.dataUrl = e.target.result;
              $scope.userData.shipProfilePicPath = $scope.thumbnail.dataUrl;

            });
          }
        });
      }
    }

  };
  $scope.addNewUser = function (businessCategory) {
    $scope.businessCategory = businessCategory;
  }
  $scope.addUser = function (user) {
    $scope.loader = true;
    if (user.password !== user.confirmPassword) {
      $scope.loader = false;

      toaster.error("error", "Password does not match")

    } else {

      $scope.userInfoId = [];
      $scope.selectedUserslist = user.selectedUsers;
      angular.forEach($scope.selectedUserslist, function (infos) {
        $scope.userInfoId.push(infos.id);
      });

      if ($scope.roleAliasList != null) {
        angular.forEach($scope.roleAliasList, function (obj) {
          if (obj.roleAliasName == $scope.businessCategory) {
            $scope.roleId = obj.roleId;
          }

        })
      }
      var userInfo = {
        "firstName": user.firstName,
        "lastName": user.lastName,
        "userName": user.userName,
        "password": user.password,
        "businessCategory": $scope.businessCategory,
        "roleId": $scope.roleId,
        "mail": user.mail,
        "organizationId": $scope.organizationId,
        "loginId": $scope.userId,
        "shipProfileIds": $scope.userInfoId
      }
      FunctionalityService.addUser(userInfo, $scope.shipPic).then(function (response) {
        $scope.loader = false;

        if (response.status == 200 || response.status == 201) {
          toaster.pop("success", response.data.message);
          callCommercial();
          callTech();
          callShipmaster();
          $scope.popupClear();

        } else {
          toaster.pop('error', response.data.message);
        }

      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });

    }
  }
  $scope.popupClear = function () {

    $scope.user.firstName = "";
    $scope.user.lastName = "";
    $scope.user.mail = "";
    $scope.user.userName = "";
    $scope.user.password = "";
    $scope.user.confirmPassword = "";
    $scope.user.selectedCategory = "";
    $scope.user.selectedUsers = "";
    $scope.user = angular.copy($scope.master);
    if ($scope.addNewUserSelectForm) $scope.addNewUserSelectForm.$setPristine();
    $scope.thumbnail.dataUrls = "image/avatarimg.jpg"
    document.getElementById("control").value = "";
    $scope.data.image = undefined;
  }
  $scope.checkPassword = function (password, confirmPassword) {
    if (password != confirmPassword) {
      $scope.IsMatch = true;
      $scope.isDisabled = true;
    }
    else if (password == confirmPassword) {
      $scope.IsMatch = false;
      $scope.newpassword = password;
      $scope.isDisabled = false;
    }
  }



  getList();
  function getList() {
    FunctionalityService.getVessellist($scope.userId).then(function (response) {
      if (response.status == 201 || response.status == 200) {
        $scope.vesselsList = response.data.shipProfileList;
        angular.forEach($scope.vesselsList, function (value) {
          $scope.commercialManagerInfoList.push(value.commercialManagerInfoList);
          $scope.techManagerInfoList.push(value.techManagerInfoList);
        })
        // $scope.vesselsList.map((value, key) => {
        //   $scope.commercialManagerInfoList.push(value.commercialManagerInfoList);
        //   $scope.techManagerInfoList.push(value.techManagerInfoList);
        // })
      }
      else if (response.status == 206) {
        toaster.pop('error', response.data.message);
      }
    })
  }

  var today = new Date();
  $scope.maxDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
  $scope.minDate = new Date(today.getFullYear() - 50, today.getMonth(), today.getDate() - 1);
  $scope.maxDate1 = new Date(today.getFullYear() + 50, today.getMonth(), today.getDate() + 1);
  $scope.minDate1 = new Date(today.getFullYear() - 50, today.getMonth(), today.getDate() - 1);

  $scope.vesselUserShow = function (tickValid) {
    $('#addplaceholder').modal('hide');
    $scope.vesselplaceholderDiv =false;
    if (Object.keys(tickValid).length === 0) {      
      $scope.vesselUser = true;
      $scope.vesselTick = false;
    }
    else {      
      $scope.vesselUser = true;
      $scope.vesselTick = true

    }

  }
  $scope.vesselplaceholder = function (tickValid) {
    if (Object.keys(tickValid).length === 0) {
      $scope.vesselModel = false;
      $scope.vesselUser = false;
      $scope.vesselTick = false;
      $scope.vesselplaceholderDiv = true;
      $scope.vesselTick = false;
    }
    else {
      $scope.vesselModel = false;
      $scope.vesselplaceholderDiv = true;
      $scope.vesselTick = true

    }

  }
  $scope.prevTovesselInfo = function () {
    $scope.vesselUser = false;
    $scope.vesselTick = false;
    $scope.vesselModel = true;
    $('#addplaceholder').modal('hide');
    $scope.vesselplaceholderDiv =false;
  }
  function callCommercial() {
    FunctionalityService.getCommercial($scope.userId).then(function (response) {
      $scope.commercialManagerInfos = response.data.commercialManagerInfos;
    });
  }
  function callTech() {
    FunctionalityService.getTech($scope.userId).then(function (response) {
      $scope.technicalManagerInfos = response.data.technicalManagerInfos;
    });
  }
  function callShipmaster() {
    FunctionalityService.getShipmaster($scope.userId).then(function (response) {
      $scope.shipMasterInfos = response.data.shipMasterInfos;
    });
  }


  FunctionalityService.getCountry().then(function (response) {
    if (response.status == 201 || response.status == 200) {
      $scope.countryInfos = response.data.countryInfos;
      angular.forEach($scope.countryInfos, function (id) {
        $scope.countryId.push(id.countryId);
      })
      // $scope.countryInfos.map((id, i) => {
      //   $scope.countryId.push(id.countryId);
      // })
    }
    else if (response.status == 206) {
      toaster.pop('error', response.data.message);

    }
  })
  FunctionalityService.getShiptype($scope.userId).then(function (response) {
    if (response.status == 201 || response.status == 200) {
      $scope.vesselsTypeInfos = response.data.vesselsTypeInfos;
    }
    else if (response.status == 206) {
      toaster.pop('error', response.data.message);

    }
  })

  $scope.countryChange = function (country) {
    $scope.jsonC = JSON.parse(country);
    FunctionalityService.getPort($scope.jsonC.countryId).then(function (response) {
      if (response.status == 201 || response.status == 200) {
        $scope.portInfos = response.data.portInfos;

      }
      else if (response.status == 206) {
        toaster.pop('error', response.data.message);
        $scope.portInfos = [];

      }
    })
  }

  $scope.thumbnail = {
    // dataUrl: 'adsfas'
  };

  $scope.fileReaderSupported = window.FileReader != null;
  $scope.uploadFileuser = function (files) {

    if (files != null) {
      var file = files[0];

      $scope.shipPic = file;
      if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
        $timeout(function () {
          var fileReader = new FileReader();
          fileReader.readAsDataURL(file);
          fileReader.onload = function (e) {
            $timeout(function () {
              $scope.thumbnail.dataUrls = e.target.result;
            });
          }
        });
      }
    }

  };

  $scope.addShipChange = function (details) {
    $scope.shipUserId = details.userId;
    // $scope.shipJson = JSON.parse(details);
    // $scope.shipUserId = $scope.shipJson.userId;
  }

  $scope.addTechChange = function (details) {
    $scope.selectedTechdetails = details;
  }

  $scope.addComChange = function (details) {
    $scope.selectedComdetails = details;
  }

  $scope.addShipSave = function (shipDetails) {
    $scope.loader = true;
    $scope.docIds = [];
    $scope.customDocumentHolders = [];
    console.log("shipDetails.selectedocu::",shipDetails.selectedocu);
    angular.forEach(shipDetails.selectedocu, function(item){
      $scope.docIds.push(item.documentHolderId);
  })   
  angular.forEach(shipDetails.selectecustom, function(item){
    var dName = item.documentHolderName;
    var distripution = item.documentHolderDescription;
    var obj = {"documentHolderName":dName,"documentHolderDescription":distripution};
    $scope.customDocumentHolders.push(obj)
})  

    if ($rootScope.errorFile === "Picture should be below 1MB") {
      toaster.pop('error', $rootScope.errorFile);

    }
    else {
      $rootScope.errorFile = '';

      if ($scope.selectedTechdetails != null) {
        angular.forEach($scope.selectedTechdetails, function (value) {
          $scope.techManagerIds.push(value.userId);
        });
        // $scope.selectedTechdetails.map((value, key) => {
        //   $scope.techManagerIds.push(value.userId)

        // })
      }
      if ($scope.selectedComdetails != null) {
        angular.forEach($scope.selectedComdetails, function (value) {
          $scope.commercialMasterIds.push(value.userId);
        });
        // $scope.selectedComdetails.map((value, key) => {
        //   $scope.commercialMasterIds.push(value.userId)

        // })
      }

      $scope.jsonCountry = JSON.parse(shipDetails.countryName);
      $scope.jsonState = JSON.parse(shipDetails.stateName);
      $scope.jsonShipType = JSON.parse(shipDetails.shipTypes);

      var data = { "bhp": shipDetails.bhp, "builder": shipDetails.builder, "callSign": shipDetails.callSign, "countryName": $scope.jsonCountry.countryName, "delivered": shipDetails.delivered, "engineType": shipDetails.engineType, "imo": shipDetails.imo, "internationalGRT": shipDetails.internationalGRT, "internationalNRT": shipDetails.internationalNRT, "keelLaid": shipDetails.keelLaid, "registeredOwner": shipDetails.registeredOwner, "shipName": shipDetails.shipName, "shipTypes": $scope.jsonShipType.vesselsTypeName, "stateName": $scope.jsonState.portName, "loginId": $scope.userId, "commercialMasterIds": $scope.commercialMasterIds, "techManagerIds": $scope.techManagerIds, "shipMasterId": $scope.shipUserId, "status": 1, "organizationId": $scope.organizationId ,"documentHolderName": shipDetails.placeholdername, "documentHolderDescription": shipDetails.placeholderdescription ,"type" : "Custom","docIds":$scope.docIds,"customDocumentHolders":$scope.customDocumentHolders};

      FunctionalityService.addShip(data, $scope.shipPic).then(function (response) {
        $scope.loader = false;

        if (response.status == 201 || response.status == 200) {
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.go('dapp.adminVessels');
          }, 500);

          getList();

        }
        else if (response.status == 206) {
          toaster.pop('error', response.data.message);
          $scope.commercialMasterIds = [];
          $scope.techManagerIds = [];
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
    }
  }
  $scope.addPlaceHolder = function () {
    
     
     var addPlaceHolderData = { "userId": $scope.userProfileId, "documentHolderName": $scope.placeholdername, "documentHolderDescription": $scope.placeholderdescription ,"organizationName":  $scope.organizationName,"type" : "Custom"};  
     $scope.allPlaceHolderListCustom.push(addPlaceHolderData);
     $scope.userData.selectecustom = $scope.allPlaceHolderListCustom;
     $('#addplaceholder').modal('hide');
     $scope.placeholdername = "";
     $scope.placeholderdescription  = ""

    // FunctionalityService.addPlaceHolder(addPlaceHolderData)
    //     .then(function mySuccess(response) {
    //         $scope.loader= false;

    //         if (response.status == 201 || response.status == 200) {
    //             $('#addplaceholder').modal('hide');
    //            // $state.reload();
    //            placeholderByCustom();
    //     $timeout(function () {
    //         toaster.clear();
    //         toaster.success({ title: response.data.message });
    //     }, 1000);

    //         } else {
    //             $('#addplaceholder').modal('hide');
    //             $state.reload();
    //             toaster.clear();
    //             toaster.error({ title: response.data.message });
    //         }
    //     }, function myError(err) {
    //         $scope.loader = false;
    //         console.log("Error response", err);
    //       });
}

$scope.clearAddPlaceholder = function (){
    $scope.placeholdername = "";
    $scope.placeholderdescription = "";
    $scope.placeholderfilename = "";
}
$scope.$on('$viewContentLoaded', function () {
  $scope.loader= true;
  var dataid = {"userId":$scope.userProfileId};
  console.log("data is ::",dataid)
  placeholderByCustom();
  FunctionalityService.getPlaceHolderListByorganizatinId(dataid)
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
  })

  function placeholderByCustom()  {
    var dataid = {"userId":$scope.userProfileId,"type":"Custom"};
    console.log("data is ::",dataid)
    FunctionalityService.getPlaceHolderListByorganizatinId(dataid)
        .then(function mySuccess(response) {
            $scope.loader= false;
  
            if (response.status == 201 || response.status == 200) {
  
                // $scope.allPlaceHolderListCustom = JSON.stringify(response.data.documentHolderList);
                // $scope.allPlaceHolderListCustom = response.data.documentHolderList;
                console.log("$scope.allPlaceHolderListCustom",JSON.stringify($scope.allPlaceHolderListCustom));
                if ($scope.allPlaceHolderListCustom == undefined) {
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
}])




  .directive('customdatepicker', [
    '$window', '$templateCache',
    function ($window, $templateCache) {

      if (!$window.moment) {
        console.log('moment.js is required for this datepicker, http://momentjs.com/');
        return {
          link: function () { }
        };
      }

      var fileName = 'datepicker.directive.html';
      var template = $templateCache.get(fileName);

      if (!template) {
        template = [
          '<form name="datepicker">',
          '<div class="btn-group btn-group-justified" role="group">',
          '<div ng-repeat="i in localeOrder track by $index" class="btn-group" role="group">',
          '<button type="button" class="btn btn-default dropdown-toggle drop-focus" data-toggle="dropdown">',
          '<span ng-bind="date[options[i].name] || options[i].name"> </span>',
          '<span class="caret"></span>',
          '</button>',

          '<ul class="dropdown-menu" role="menu">',
          '<li ng-repeat="(j, option) in options[i].options track by $index" ng-class="{\'selectedval\': option.selected === true, \'disabled\': option.disabled === true}">',
          '<a ng-click="select(options[i].name, option)" ng-bind="options[i].labels[j] || option.value"></a>',
          '</li>',
          '</ul>',

          '</div>',
          '</div>',
          '</form>'

        ].join('');
        $templateCache.put(fileName, template);
      }

      return {
        restrict: 'A',
        //replace: true,
        require: 'ngModel',
        templateUrl: fileName,
        scope: {
          model: '=ngModel',
          minDate: '=minDate',
          maxDate: '=maxDate',
          locale: '=?locale'
        },
        link: function (scope, elem, attrs, ngModelCtrl) {

          var day = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31]
          var month = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
          scope.localeOrder = ['day', 'month', 'year'];


          // params object is the main object to be modified. It is iterated by ng-repeat in the dom. 
          var params = {
            day: {
              name: 'day',
              initLabel: 'Day',
              options: []
            },
            month: {
              name: 'month',
              initLabel: 'Month',
              options: [],
            },
            year: {
              name: 'year',
              initLabel: 'Year',
              options: []
            }
          };


          // setting up exceptions because of minDate, maxDate; 
          var minDate;
          var maxDate;

          // setting minDate
          if (scope.minDate && scope.minDate !== 'now') {

            var tempMinDate;

            if (typeof scope.minDate === 'number') {
              // assume this is something like -100 or +100; 
              if (scope.minDate > 0) {
                tempMinDate = moment().add(scope.minDate, 'year');
              } else if (scope.minDate < 0) {
                tempMinDate = moment().subtract(Math.abs(scope.minDate), 'year');
              } else {
                tempMinDate = moment();
              }
            } else {
              tempMinDate = moment(scope.minDate);
            }


            // setting it to minDate
            minDate = tempMinDate;

          } else {
            minDate = moment();
          }


          // setting maxDate. THIS IS NOT DRY, BUT A PASTE FROM SETTING minDate !
          if (scope.maxDate && scope.maxDate !== 'now') {

            var tempMaxDate;

            if (typeof scope.maxDate === 'number') {
              // assume this is something like -100 or +100; 
              if (scope.maxDate > 0) {
                tempMaxDate = moment().add(scope.maxDate, 'year');
              } else if (scope.maxDate < 0) {
                tempMaxDate = moment().subtract(Math.abs(scope.maxDate), 'year');
              } else {
                tempMaxDate = moment();
              }
            } else {
              tempMaxDate = moment(scope.maxDate);
            }


            // setting it to maxDate
            maxDate = tempMaxDate;

          } else {
            maxDate = moment();
          }


          // copying scope.options, which is used in view.
          scope.options = angular.copy(params);


          // holder for date object, modified by scope.select(). Every change the method setupModel() is called, a new Date is created from this. 
          scope.date = {};


          // Select the value; 
          scope.select = function (typeString, option) {

            if (option.disabled === undefined || option.disabled !== true) {
              scope.date[typeString] = option.value;
              calcAvailableDates(scope.date);
              setupModel();
            }
          };


          // recreate scope.options based on params and date input;
          function calcAvailableDates(dateObj) {



            var maxYear = maxDate.get('year')
            var minYear = minDate.get('year');
            var maxMonth = maxDate.get('month');
            var minMonth = minDate.get('month');
            var maxDay = maxDate.get('date');
            var minDay = minDate.get('date');

            // CALCULATE YEARS
            if (minDate !== maxDate) {

              scope.options.year.options = [];
              for (var i = minYear; i <= maxYear; i++) {

                var yearObj = {
                  value: i,
                  available: true
                };

                if (dateObj && dateObj.year === i) {
                  yearObj.selected = true;
                }

                scope.options.year.options.push(yearObj);
              }

            } else {
              // do something like assumptions, e.g. datepicker? 
            }


            // CALCULATE MONTHS
            if (minDate !== maxDate) {

              scope.options.month.options = [];

              for (var i = 0; i < month.length; i++) {

                var monthObj = {
                  value: month[i]
                };

                if (dateObj && dateObj.month === month[i]) {
                  monthObj.selected = true;
                }

                if (dateObj && dateObj.year) {
                  if (dateObj.year === minYear) {
                    if (minMonth > i) {
                      monthObj.disabled = true;
                    }
                  } else if (dateObj.year === maxYear) {
                    if (maxMonth < i) {
                      monthObj.disabled = true;
                    }
                  }
                }

                scope.options.month.options.push(monthObj);
              }

            } else {
              // do something like assumptions, e.g. datepicker? 
            }



            // CALCULATE DAYS
            if (minDate !== maxDate) {
              scope.options.day.options = [];
              if (dateObj) {
                // if ((dateObj && dateObj.years && dateObj.months) || dateObj.months) {

                var useYear;
                if (dateObj.year) {
                  useYear = dateObj.year
                } else {
                  useYear = moment().get('year');
                }

                var totalDaysMonth = moment({
                  year: useYear,
                  month: month.indexOf(dateObj.month)
                }).endOf('month').get('date');

                for (var i = 0; i < totalDaysMonth; i++) {

                  var dayObj = {}
                  dayObj.value = i + 1;

                  if (dateObj && dateObj.day && dateObj.day === i + 1) {
                    dayObj.selected = true;
                  }


                  // setting values disabled when they're mindate < or > maxDate

                  if (dateObj.year && dateObj.month) {
                    if (dateObj.year === minYear && month.indexOf(dateObj.month) === minMonth) {

                      if (minDay > i) {
                        dayObj.disabled = true;
                      }
                    } else if (dateObj.year === maxYear && month.indexOf(dateObj.month) === maxMonth) {

                      if (maxDay < i) {
                        dayObj.disabled = true;
                      }
                    }
                  }

                  scope.options.day.options.push(dayObj);

                }


              } else {

                // only days available, just use 31 days. 
                scope.options.day.options.push(dayObj);
                for (var i = 0; i < day.length; i++) {

                  var dayObj = {}
                  dayObj.value = i + 1;

                  if (dateObj && dateObj.day && dateObj.day === i + 1) {
                    dayObj.selected = true;
                  }

                  scope.options.day.options.push(dayObj);
                }
              }

            }


          }



          function setupModel() {

            // Not setting before all values are available. 
            if (scope.date.year !== undefined && scope.date.month !== undefined && scope.date.day !== undefined) {

              var createDate = {
                year: scope.date.year,
                month: month.indexOf(scope.date.month),
                date: scope.date.day,
              };

              createDate = moment.utc(createDate);
              ngModelCtrl.$setViewValue(createDate)
            }
          }


          // toView - what is transformed from model to view. 
          var toView = function (value) {

            if (value !== undefined) {
              var tempDate = moment(value);
              calcAvailableDates({
                year: tempDate.get('year'),
                month: month[tempDate.get('month')],
                day: tempDate.get('date')
              });
            } else {
              calcAvailableDates();
            }


            if (value !== undefined) {
              var createDate = moment(value);
              if (createDate.isValid()) {

                var obj = {};
                obj.day = createDate.date();
                obj.month = month[createDate.month()];
                obj.year = createDate.year();

                scope.date = obj;

              }
            } else {
              scope.date = {};
            }

            setupModel();

            return value;

          };


          // toModel - what is transformed from view to model (called by ngModelCtrl.$setViewValue(createDate))
          // in setupModel()
          var toModel = function (value) {

            console.log('toModel ', value)

            if (value && value.isValid()) {
              if ((value.isAfter(minDate) || minDate.isSame(value)) && (maxDate.isAfter(value) || maxDate.isSame(value))) {
                ngModelCtrl.$setValidity('not-allowed', true);
                return value.format();
              } else {
                ngModelCtrl.$setValidity('not-allowed', false);
                return undefined;
              }
            } else {
              return undefined;
            }

          };

          // Pushing functions to $parsers and $formatters.
          ngModelCtrl.$parsers.push(toModel);
          ngModelCtrl.$formatters.push(toView);

        }
      }

    }
  ]).directive('customFocus', [function () {
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
  }]).directive('number', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attrs, ctrl) {
        ctrl.$parsers.push(function (input) {
          if (input == undefined) return ''
          var inputNumber = input.toString().replace(/[^0-9]/g, '');
          if (inputNumber != input) {
            ctrl.$setViewValue(inputNumber);
            ctrl.$render();
          }
          return inputNumber;
        });
      }
    };
  });
