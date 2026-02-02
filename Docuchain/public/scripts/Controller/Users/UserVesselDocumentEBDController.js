// var userVesselDocEBD = angular.module('dapp.UserVesselDocumentEBDController', [
//   'ui.select',
//   'angularUtils.directives.dirPagination',
//   'toaster',
//   'moment-picker',
//   '720kb.tooltips',
//   'ngSanitize',
// ]);

// userVesselDocEBD.controller('UserVesselDocumentEBDController', [
//   '$scope',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   '$timeout',
//   'toaster',
//   '$filter',
//   '$sce',
//   'FunctionalityService',
//   function (
//     $scope,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     $timeout,
//     toaster,
//     $filter,
//     $sce,
//     FunctionalityService
//   ) {
//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );
//     $scope.libShipId;
//     $scope.libshipName;
//     $scope.loader = false;
//     $scope.group = {};
//     $scope.vesselSelectedList = [];
//     $scope.formattedIssueDate = '';
//     $scope.formattedExpiryDate = '';
//     $scope.formattedlastAnnual = '';
//     $scope.formattednextAnnual = '';
//     $scope.issuingAuthority = '';
//     $scope.notfound = true;
//     $scope.currentPage = 1;
//     $scope.viewby = 73;
//     $scope.roleIdForView = $scope.sessionObject.roleId;
//     $scope.itemsPerPageForHistory = $scope.viewby;
//     itemsPerPageHistory = $scope.viewby;
//     $scope.activeText = 'Active';
//     if ($scope.sessionObject.roleId != 3) {
//       $scope.libShipId = $window.localStorage.getItem('libShipId');
//       $scope.libshipName = $window.localStorage.getItem('libshipName');
//       $scope.libshipNameDoc = $scope.libshipName + ' ' + 'Documents';
//     } else if ($scope.sessionObject.shipProfileInfos.length > 0) {
//       $scope.libShipId = $scope.sessionObject.shipProfileInfos[0].id;
//       $scope.libshipName = $scope.sessionObject.shipProfileInfos[0].shipName;

//       // angular.forEach($scope.sessionObject.shipProfileInfos, function(val) {
//       //     console.log("shipProfileInfos[0].shipName::"+val);
//       //     $scope.vesselSelectedList.push(val);
//       // })
//       // $scope.group.vesselSelectedList = $scope.vesselSelectedList;
//     }

//     $scope.selctedcheckboxlst = [];
//     $scope.expiryDocumentHolderList = [];
//     $scope.expiryDocumentHolderListLen = $scope.expiryDocumentHolderList.length;

//     $scope.viewDocumentObjectInfo;
//     $scope.viewDocumentUrl;
//     $scope.historyEBDObject;

//     $rootScope.convertedFile = '';
//     $rootScope.uploadDocFile;
//     $scope.loader = false;
//     $rootScope.dcoumentHolderName;
//     $rootScope.docId;
//     $scope.documentHolderHistory = [];
//     $scope.documentHolderHistoryLength = $scope.documentHolderHistory.length;
//     $scope.groupTagExpiryDoc;
//     $scope.groupListExpiry;
//     $scope.shareExpDoc = [];
//     $scope.records = false;

//     $scope.selectedDocumentHolderIds = [];
//     $scope.oldAndNewExpDocWithoutDub = [];
//     $scope.vesselList = [];
//     $scope.expiryDocumentHolderListFileter = [];
//     $scope.VesselListLenEBD = $scope.vesselList.length;

//     console.log(
//       "$window.localStorage.getItem('userId');",
//       $window.localStorage.getItem('userId')
//     );

//     $scope.getAllExpiryList = function () {
//       $scope.loader = true;
//       var archivedStatus = 0;
//       FunctionalityService.getAllExpiryDocumentList(
//         $scope.libShipId,
//         archivedStatus
//       ).then(
//         function (response) {
//           if (response.status == 200 || response.status == 201) {
//             $scope.expiryDocumentHolderList = response.data.expiryDocumentList;
//             $scope.expiryDocumentHolderListFileter =
//               $scope.expiryDocumentHolderList;
//             $scope.expiryDocumentHolderListLen =
//               $scope.expiryDocumentHolderList.length;
//             $scope.loader = false;
//             console.log(
//               'response.data.expiryDocumentList' +
//                 JSON.stringify($scope.expiryDocumentHolderList)
//             );
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           if ($scope.expiryDocumentHolderList.length == 0) {
//             $scope.records = true;
//           } else {
//             $scope.records = false;
//           }
//           console.log('Error response', err);
//         }
//       );
//       var data1 = {
//         userId: $scope.sessionObject.userId,
//         roleId: $scope.sessionObject.roleId,
//       };
//       FunctionalityService.getVesselProfileList(data1).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             angular.forEach(response.data.shipProfileList, function (val) {
//               if (val.id != $scope.libShipId) $scope.vesselList.push(val);
//             });
//             //  $scope.vesselList = response.data.shipProfileList;

//             // $scope.vesselListLength = $scope.vesselList.length;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.getAllGroupList = function () {
//       $scope.loader = true;

//       var groupdata = {
//         userProfileId: $scope.sessionObject.userId,
//         shipId: $scope.libShipId,
//       };

//       FunctionalityService.getGroupListShip(groupdata).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $scope.message = JSON.stringify(response.data.groupList);
//             $scope.groupList = response.data.groupList;
//             $scope.groupListLength = $scope.groupList.length;
//           }
//           // else {
//           //     toaster.clear();
//           //     toaster.error({ title: response.data.message});
//           // }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );

//       //     FunctionalityService.getGroupList($scope.sessionObject.userId)
//       //         .then(function (response) {
//       //             $scope.loader = false;

//       //             if (response.status == 200) {
//       //                 $scope.message = JSON.stringify(response.data.groupList);
//       //                 $scope.groupList = response.data.groupList;
//       //                 $scope.groupListLength = $scope.groupList.length;
//       //             } else {
//       //                 toaster.clear();
//       //                 toaster.error({ title: response.data.message });
//       //             }
//       //         }, function myError(err) {
//       //             $scope.loader = false;
//       //             console.log("Error response", err);
//       //           });
//     };

//     $scope.faq = function () {
//       $state.go('dapp.faq');
//     };

//     //Thi method is used for file scan file file and open the create popup
//     $scope.uploadFile = function () {
//       toaster.clear();
//       $rootScope.convertedFile = '';
//       $scope.isDisabled = true;
//       if ($scope.myFile != undefined) {
//         $scope.loader = true;
//         var file = $scope.myFile;
//         if ($scope.myFile.size < 20971520) {
//           if ($scope.myFile != undefined) {
//             $rootScope.uploadDocFile = $scope.myFile;
//             $rootScope.convertedFile = $sce.trustAsResourceUrl(
//               URL.createObjectURL($scope.myFile)
//             );

//             FunctionalityService.scanExpiryDocument(
//               $rootScope.uploadDocFile
//             ).then(
//               function (response) {
//                 $scope.loader = false;

//                 if (response.status == 200 || response.status == 201) {
//                   $('#fileupload').modal('hide');
//                   $('#filename').val('');
//                   //var issuedate = $filter('date')(response.data.expiryDocumentDTOs.issueDate,'MMM dd, yyyy');
//                   //var expiryDate = $filter('date')(response.data.expiryDocumentDTOs.expiryDate,'MMM dd, yyyy');
//                   if (
//                     response.data.expiryDocumentDTOs != null &&
//                     Object.keys(response.data.expiryDocumentDTOs).length != 0
//                   ) {
//                     $scope.libcreate.certificateNumber =
//                       response.data.expiryDocumentDTOs.certificateNumber;
//                     $scope.libcreate.placeOfIssue =
//                       response.data.expiryDocumentDTOs.placeOfIssue;
//                     $scope.libcreate.dateOfIssue =
//                       response.data.expiryDocumentDTOs.issueDate;
//                     $scope.libcreate.dateOfExpiry =
//                       response.data.expiryDocumentDTOs.expiryDate;
//                     //$scope.libcreate.issuingAuthority = response.data.expiryDocumentDTOs.issuingAuthority;
//                   }
//                   $('#createEBD').modal('toggle');
//                 } else {
//                   $scope.loader = false;
//                   $scope.scanfail = false;
//                   $('#createEBD').modal('toggle');
//                 }
//               },
//               function myError(err) {
//                 $scope.loader = false;
//                 console.log('Error response', err);
//               }
//             );
//           } else {
//             $scope.loader = false;
//             $('#filename').val('');
//             $scope.scanfail = true;
//             toaster.clear();
//             toaster.error({ title: 'Please choose file' });
//             // $scope.scanMsg = "Please choose file"
//             // $timeout(function () {
//             //     $scope.isDisabled = false;
//             //     $scope.scanfail = false;
//             // }, 1000);
//           }
//         } else {
//           $scope.loader = false;
//           $('#filename').val('');
//           toaster.clear();
//           toaster.info({ title: 'Please choose less than 5Mb file' });
//         }
//       } else {
//         $scope.loader = false;
//         toaster.error('Please choose a file');
//       }
//     };

//     //This method is used to clear the file upload fields
//     $scope.clearFileField = function () {
//       $('#filename').val('');
//       $scope.loader = false;
//       $scope.isDisabled = false;
//     };

//     //Store document gobally while uploading
//     $scope.storeDocumentholder = function (obj) {
//       $rootScope.dcoumentHolderName = obj.documentHolderName;
//       $rootScope.docId = obj.documentHolderId;
//       $scope.clearFileField();
//     };

//     //This method is used to create Document
//     $scope.createDocument = function () {
//       $scope.loader = true;

//       //var dt=$scope.libcreate.dateOfExpiry;

//       $scope.formattedIssueDate = $filter('date')(
//         $scope.libcreate.dateOfIssue,
//         'yyyy-MM-dd'
//       );
//       $scope.formattedExpiryDate = $filter('date')(
//         $scope.libcreate.dateOfExpiry,
//         'yyyy-MM-dd'
//       );
//       $scope.saveData = {
//         certificateNumber: $scope.libcreate.certificateNumber,
//         placeOfIssue: $scope.libcreate.placeOfIssue,
//         issueDate: $scope.formattedIssueDate,
//         expiryDate: $scope.formattedExpiryDate,
//         lastAnnual: $scope.libcreate.lastAnnual,
//         nextAnnual: $scope.libcreate.nextAnnual,
//         uploadedUserId: $scope.sessionObject.userId,
//         loginId: $scope.sessionObject.userId,
//         shipProfileId: $scope.libShipId,
//         documentHolderId: $rootScope.docId,
//         remarks: $scope.libcreate.remarks,
//         issuingAuthority: $scope.issuingAuthority,
//       };
//       FunctionalityService.saveExpiryDocument(
//         JSON.stringify($scope.saveData),
//         $rootScope.uploadDocFile
//       ).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $('#createEBD').modal('hide');
//             $('#filename').val('');
//             $rootScope.uploadDocFile = '';
//             $scope.clearFields();
//             $scope.getAllExpiryList();
//             toaster.clear();
//             toaster.success({ title: response.data.message });
//           } else {
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.dateConversion = function (day) {
//       var stringDate1 = day;
//       var splitDate1 = stringDate1.split('-');
//       var day1 = splitDate1[0];
//       var month1 = splitDate1[1];
//       var year1 = splitDate1[2];
//       this.dStartDate = year1 + '-' + month1 + '-' + day1;
//       return this.dStartDate;
//     };
//     $scope.cancelExpiry = function () {
//       $('#viewExpiryDocument').modal('hide');
//       $state.reload();
//     };
//     //This function is used for clear all fields while adding
//     $scope.updateExpiryDoc = function () {
//       if (
//         $scope.issueDateStringEdit != '' &&
//         $scope.issueDateStringEdit != undefined
//       )
//         $scope.formattedIssueDate = $scope.dateConversion(
//           $scope.issueDateStringEdit
//         );
//       if (
//         $scope.expiryDateStringEdit != '' &&
//         $scope.expiryDateStringEdit != undefined
//       )
//         $scope.formattedExpiryDate = $scope.dateConversion(
//           $scope.expiryDateStringEdit
//         );
//       if (
//         $scope.lastAnnualStringEdit != '' &&
//         $scope.lastAnnualStringEdit != undefined
//       )
//         $scope.formattedlastAnnual = $scope.dateConversion(
//           $scope.lastAnnualStringEdit
//         );
//       if (
//         $scope.nextAnnualStringEdit != '' &&
//         $scope.nextAnnualStringEdit != undefined
//       )
//         $scope.formattednextAnnual = $scope.dateConversion(
//           $scope.nextAnnualStringEdit
//         );
//       var data = {
//         id: $scope.docId,
//         certificateNumber: $scope.certificateNumberEdit,
//         placeOfIssue: $scope.placeOfIssueEdit,
//         issueDate: $scope.formattedIssueDate,
//         expiryDate: $scope.formattedExpiryDate,
//         lastAnnual: $scope.formattedlastAnnual,
//         nextAnnual: $scope.formattednextAnnual,
//         //"uploadedUserId":$window.localStorage.getItem('userId'),
//         loginId: $window.localStorage.getItem('userId'),
//         uploadedUserId: $window.localStorage.getItem('userId'),
//         shipProfileId: $scope.libShipId,
//         documentHolderId: $scope.docHolderId,
//         remarks: $scope.remarksEdit,
//         issuingAuthority: $scope.issuingAuthorityEdit,
//       };
//       console.log('data of update doc::' + JSON.stringify(data));
//       FunctionalityService.updateExpiryDocument(data).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $('#viewExpiryDocument').modal('hide');

//             $state.reload();
//             //$state.go('dapp.userVesselDocumentEBD');
//             $timeout(function () {
//               toaster.clear();
//               toaster.success({ title: response.data.message });
//             }, 1000);
//           } else {
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           toaster.error({ title: response.data.message });
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.clearFields = function () {
//       $scope.libcreate.certificateNumber = '';
//       $scope.libcreate.placeOfIssue = '';
//       $scope.libcreate.dateOfIssue = '';
//       $scope.libcreate.dateOfExpiry = '';
//       $scope.libcreate.lastAnnual = '';
//       $scope.libcreate.nextAnnual = '';
//       $scope.libcreate.remarks = '';
//       $scope.issuingAuthority = '';
//       $('#filename').val('');
//       $scope.loader = false;
//       $scope.uploadDocFile = '';
//     };

//     $scope.closeViewPopup = function () {
//       // $window.location.reload();
//       //$state.reload();
//       //    $('#embedContainer').reload();
//       //delete $scope.viewDocumentUrl;
//       //$('#viewExpiryDocument').remove();
//       //$('#viewExpiryDocument').hide();
//       //$state.reload();
//     };
//     //This method is used view Document information
//     $scope.viewExpiryDocumentInformation = function (documentObj) {
//       $timeout(function () {
//         //$('#embedContainer').show();
//         $('#viewExpiryDocument').modal('show');

//         $scope.viewDocumentObjectInfo = documentObj;
//         $scope.docId = documentObj.id;
//         $scope.docHolderId = documentObj.documentHolderId;
//         $scope.viewDocumentUrl = $sce.trustAsResourceUrl(
//           $scope.viewDocumentObjectInfo.documentPreviewUrl
//         );
//         console.log($scope.viewDocumentUrl);
//         var pdfViewerEmbed = document.getElementById('embedContainer');
//         pdfViewerEmbed.setAttribute('src', $scope.viewDocumentUrl);
//         pdfViewerEmbed.outerHTML = pdfViewerEmbed.outerHTML.replace(
//           /src="(.+?)"/,
//           'src="' + $scope.viewDocumentUrl + '"'
//         );
//         // $('#embedContainer').html(' <div class="pdficondiv" id="embedContainer"><embed ng-src="{{viewDocumentUrl}}" width="500"></div>');
//         $scope.certificateNumberEdit =
//           $scope.viewDocumentObjectInfo.certificateNumber;
//         $scope.issuingAuthorityEdit =
//           $scope.viewDocumentObjectInfo.issuingAuthority;
//         // $scope.issueDateStringEdit = $scope.viewDocumentObjectInfo.expiryDateString;
//         $scope.placeOfIssueEdit = $scope.viewDocumentObjectInfo.placeOfIssue;
//         $scope.issueDateStringEdit =
//           $scope.viewDocumentObjectInfo.issueDateString;
//         $scope.expiryDateStringEdit =
//           $scope.viewDocumentObjectInfo.expiryDateString;
//         $scope.lastAnnualStringEdit =
//           $scope.viewDocumentObjectInfo.lastAnnualString;
//         $scope.nextAnnualStringEdit =
//           $scope.viewDocumentObjectInfo.nextAnnualString;
//         $scope.remarksEdit = $scope.viewDocumentObjectInfo.remarksEdit;
//       }, 1000);
//     };

//     //This Method is used to get history for particular document
//     $scope.openHistoryPopup = function (documentObj) {
//       $scope.loader = true;

//       $('#historyEBD').modal('toggle');
//       $scope.historyEBDObject = documentObj;
//       var historyRequest = {
//         documentHolderId: documentObj.documentHolderId,
//         shipProfileId: $scope.libShipId,
//       };
//       FunctionalityService.getDocumentHolderHistory(historyRequest).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $scope.documentHolderHistory = response.data.expiryDocumentList;
//             $scope.documentHolderHistoryLength =
//               $scope.documentHolderHistory.length;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     //This method is used for open Group tag EBD
//     $scope.openGroupTagEBDPopup = function (expiryDataObj) {
//       $scope.loader = true;

//       $scope.groupTagExpiryDoc = expiryDataObj;

//       var groupdata = {
//         userProfileId: $scope.sessionObject.userId,
//         shipId: $scope.libShipId,
//       };

//       FunctionalityService.getGroupListShip(groupdata).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $scope.message = JSON.stringify(response.data.groupList);
//             $scope.groupListExpiry = response.data.groupList;
//             $scope.groupListExpiryLength = $scope.groupListExpiry.length;
//           }
//           // else {
//           //     toaster.clear();
//           //     toaster.error({ title: response.data.message});
//           // }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//       // FunctionalityService.getGroupList($scope.sessionObject.userId)
//       //     .then(function (response) {
//       //         $scope.loader = false;

//       //         if (response.status == 200) {
//       //             $scope.message = JSON.stringify(response.data.groupList);
//       //             $scope.groupListExpiry = response.data.groupList;
//       //         }
//       //     }, function myError(err) {
//       //         $scope.loader = false;
//       //         console.log("Error response", err);
//       //       });
//     };

//     //This method is used to tag the expiry document into the group
//     $scope.tagExpiryDocumentToGRoup = function () {
//       $scope.loader = true;

//       var addExpiryDocToGroupData = {
//         groupId: $scope.gorupSelected,
//         userProfileId: $scope.sessionObject.userId,
//         documentHolderId: $scope.groupTagExpiryDoc.documentHolderId,
//         loginId: $scope.sessionObject.userId,
//       };
//       FunctionalityService.addExpiryDocToGroup(
//         JSON.stringify(addExpiryDocToGroupData)
//       ).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $('#tagGroupPopup').modal('hide');
//             $state.reload();
//             $timeout(function () {
//               toaster.clear();
//               toaster.success({ title: response.data.message });
//             }, 1000);
//           } else {
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.clearTagDocumentFields = function () {
//       $scope.gorupSelected = '';
//     };

//     $scope.close = function () {
//       $state.reload();
//     };

//     $scope.selctedExpiredDocumentList = [];

//     $scope.checkUncheckAll = function () {
//       $scope.selctedExpiredDocumentList = [];
//       $scope.selectedDocumentHolderIds = [];
//       if ($scope.checkall) {
//         $scope.checkall = true;
//         angular.forEach($scope.expiryDocumentHolderList, function (value) {
//           if (value.id != undefined) {
//             $scope.selctedExpiredDocumentList.push(value);
//             $scope.selectedDocumentHolderIds.push(value.documentHolderId);
//           }
//         });
//       } else {
//         $scope.checkall = false;
//       }
//       angular.forEach($scope.expiryDocumentHolderList, function (user) {
//         user.checked = $scope.checkall;
//       });
//     };

//     $scope.updateCheckall = function ($index, user) {
//       var userTotal = $scope.expiryDocumentHolderList.length;
//       var count = 0;
//       $scope.selctedExpiredDocumentList = [];
//       $scope.selectedDocumentHolderIds = [];
//       angular.forEach($scope.expiryDocumentHolderList, function (item) {
//         if (item.checked) {
//           count++;
//           $scope.selctedExpiredDocumentList.push(item);
//           $scope.selectedDocumentHolderIds.push(item.documentHolderId);
//         }
//       });
//       if (userTotal == count) {
//         $scope.checkall = true;
//       } else {
//         $scope.checkall = false;
//       }
//     };
//     $scope.groupExpiryDoclist = [];
//     $scope.viewGroup = function (group) {
//       $scope.loader = true;

//       $scope.groupEmailForShare = group.emailId;
//       FunctionalityService.viewGroup(group.id).then(
//         function (response) {
//           $scope.loader = false;

//           $scope.groupIdForShare = group.id;
//           $scope.groupExpiryDoclist =
//             response.data.groupInfo.expiryDocumentDtos;
//           $scope.showUpdateIngroupCheck = true;
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//       $scope.groupIdForShare = group.id;
//     };

//     $scope.IsVisible = false;
//     $scope.ShowCreateGroup = function () {
//       $scope.IsVisible = true;
//       $scope.groupExpiryDoclist = [];
//       $scope.groupIdForShare = '';
//       $scope.showUpdateIngroupCheck = false;
//     };
//     $scope.createbutton = false;
//     $scope.showgroup = false;
//     $scope.showTable = function () {
//       if ($scope.groupSearch != undefined) {
//         if ($scope.groupSearch != '') {
//           $scope.showUpdateIngroupCheck = false;
//           $scope.showgroup = true;
//         }
//       }
//     };
//     $scope.createbutton = false;
//     $scope.showUpdateIngroupCheck = false;

//     $scope.$watch('groupSearch', function (query) {
//       $scope.groupSearchlength = $filter('filter')($scope.groupList, query);
//       if ($scope.groupSearchlength <= 0) {
//         $scope.createbutton = true;
//         $scope.groupExpiryDoclist = [];
//         $scope.showUpdateIngroupCheck = false;
//       } else {
//         $scope.createbutton = false;
//         $scope.IsVisible = false;
//         $scope.showUpdateIngroupCheck = false;
//         // $scope.groupList = [];
//         $scope.showgroup = false;
//         $scope.groupExpiryDoclist = [];
//       }
//     });

//     $scope.expirydata = [];
//     $scope.getExpiryDocument = function (ship) {
//       $scope.loader = true;

//       var archivedStatus = 0;
//       FunctionalityService.getAllExpiryDocumentList(ship, archivedStatus).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $scope.expirydata = [];
//             angular.forEach(response.data.expiryDocumentList, function (value) {
//               if (value.id != null && value.documentStatus == 'Approved')
//                 $scope.expirydata.push(value);
//             });
//             $scope.totalItems = $scope.expirydata.length;
//           } else {
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.sharePopup = function () {
//       if ($scope.selctedExpiredDocumentList.length > 0) {
//         $('#sharepopup').modal('toggle');
//         $scope.selctedcheckboxlst = $scope.selctedcheckboxlst.concat(
//           $scope.selctedExpiredDocumentList
//         );

//         //$scope.selctedcheckboxlst.push( $scope.selctedExpiredDocumentList);
//         $scope.selecetedCheckBoxValue = function (
//           selctedExpiredDocumentList,
//           active
//         ) {
//           if (active) {
//             $scope.selctedcheckboxlst.push(selctedExpiredDocumentList);
//           } else {
//             $scope.selctedcheckboxlst.splice(
//               $scope.selctedcheckboxlst.indexOf(selctedExpiredDocumentList),
//               1
//             );
//           }
//         };
//       } else {
//         toaster.clear();
//         toaster.info({
//           title: 'Please select the document before click share',
//         });
//       }
//     };

//     $scope.shipIds = [];
//     //$scope.vesselSelectedList==[];

//     $scope.shareexpirydocument = function (group) {
//       $('#shareMail').modal('toggle');
//       console.log('$scope.vesselInfo::' + $scope.libshipName);
//       if ($scope.groupIdForShare != '') {
//         if ($scope.groupIdForShare != undefined) {
//           $scope.checkedUsers = [];
//           angular.forEach($scope.groupExpiryDoclist, function (user) {
//             if (user.Selected) {
//               if ($scope.checkedUsers != '') {
//                 $scope.checkedUsers += ' , ';
//               }
//               $scope.shareExpDoc.push(user);
//             }
//           });

//           $scope.oldAndNewExpDocWithoutDub = [];
//           $scope.oldAndNewExpDoc = $scope.selctedcheckboxlst.concat(
//             $scope.shareExpDoc
//           );
//           angular.forEach($scope.oldAndNewExpDoc, function (value, key) {
//             var exists = false;
//             angular.forEach(
//               $scope.oldAndNewExpDocWithoutDub,
//               function (val2, key) {
//                 if (angular.equals(value.id, val2.id)) {
//                   exists = true;
//                 }
//               }
//             );
//             if (exists == false && value.id != '') {
//               $scope.oldAndNewExpDocWithoutDub.push(value);
//             }
//           });

//           $scope.documentHolderIds = [];
//           angular.forEach($scope.oldAndNewExpDoc, function (value, key) {
//             $scope.documentHolderIds.push(value.documentHolderId);
//           });
//           if ($scope.updateInGroup == true) {
//             var addexdoctogroup = {
//               groupId: $scope.groupIdForShare,
//               userProfileId: $scope.sessionObject.userId,
//               documentHolderIds: $scope.documentHolderIds,
//               loginId: $scope.sessionObject.userId,
//             };

//             FunctionalityService.updateshareExpiryDoc(
//               JSON.stringify(addexdoctogroup)
//             ).then(
//               function (response) {
//                 $scope.loader = false;

//                 if (response.status == 200) {
//                   $scope.expDocUrl = '';
//                   angular.forEach(
//                     $scope.oldAndNewExpDocWithoutDub,
//                     function (value, key) {
//                       $scope.expDocUrl =
//                         $scope.expDocUrl +
//                         '\n' +
//                         (value.documentHolderName +
//                           ': \n' +
//                           value.documentDownloadUrl +
//                           '\n');
//                     }
//                   );
//                   $state.reload();
//                   $('#sharepopup').modal('hide');
//                   $scope.url =
//                     'mailto:' +
//                     '?subject=' +
//                     $scope.libshipName +
//                     '%20Documents' +
//                     '&body=%0D%0A%0D%0ABelow are the list of document attached.%0D%0A%0D%0A' +
//                     encodeURIComponent($scope.expDocUrl) +
//                     '%0D%0A%0D%0A%0D%0A%0D%0A%0D%0ANote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//                   window.location.href = $scope.url;
//                   $scope.expDocUrlshare =
//                     $scope.expDocUrl +
//                     '\n\nNote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//                 } else {
//                   toaster.error('Problem in sharing expiry document');
//                 }
//               },
//               function myError(err) {
//                 $scope.loader = false;
//                 console.log('Error response', err);
//               }
//             );
//           } else {
//             // $scope.expDocUrl = "";
//             // angular.forEach($scope.selctedcheckboxlst, function (value, key) {
//             //     $scope.expDocUrl = $scope.expDocUrl + ("\n" + value.documentHolderName + ": \n" + value.documentDownloadUrl + "\n");
//             // });
//             // var mailToLink = $scope.groupSearch;
//             // $scope.url = 'mailto:' + mailToLink + '?subject=' + 'Share Expiry Document' + '&body=HI%20Dear/Madam,%20' + $window.encodeURIComponent($scope.expDocUrl)
//             // window.location.href = $scope.url;

//             $scope.expDocUrl = '';
//             angular.forEach(
//               $scope.oldAndNewExpDocWithoutDub,
//               function (value, key) {
//                 $scope.expDocUrl =
//                   $scope.expDocUrl +
//                   '\n' +
//                   (' \n' +
//                     value.documentHolderName +
//                     ': \n' +
//                     value.documentDownloadUrl +
//                     '\n');
//               }
//             );
//             $state.reload();
//             $('#sharepopup').modal('hide');
//             $scope.url =
//               'mailto:' +
//               '?subject=' +
//               $scope.libshipName +
//               '%20Documents' +
//               '&body=%0D%0A%0D%0ABelow are the list of document attached.%0D%0A%0D%0A' +
//               encodeURIComponent($scope.expDocUrl) +
//               '%0D%0A%0D%0A%0D%0A%0D%0A%0D%0ANote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//             window.location.href = $scope.url;
//           }
//         } else {
//           $scope.expDocUrl = '';
//           angular.forEach($scope.selctedcheckboxlst, function (value, key) {
//             $scope.expDocUrl =
//               $scope.expDocUrl +
//               '\n' +
//               ('\n' +
//                 value.documentHolderName +
//                 ': \n' +
//                 value.documentDownloadUrl +
//                 '\n');
//           });
//           var mailToLink = $scope.groupSearch;
//           $scope.url =
//             'mailto:' +
//             '?subject=' +
//             $scope.libshipName +
//             '%20Documents' +
//             '&body=%0D%0A%0D%0ABelow are the list of document attached.%0D%0A%0D%0A' +
//             $window.encodeURI($scope.expDocUrl) +
//             '%0D%0A%0D%0A%0D%0A%0D%0A%0D%0ANote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//           location.href =
//             'mailto:' +
//             '?subject=' +
//             $scope.libshipName +
//             '%20Documents' +
//             '&body=%0D%0A%0D%0ABelow are the list of document attached.%0D%0A%0D%0A' +
//             $window.encodeURI($scope.expDocUrl) +
//             '%0D%0A%0D%0A%0D%0A%0D%0A%0D%0ANote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//           $scope.expDocUrlshare =
//             $scope.expDocUrl +
//             '\n\nNote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//         }
//       } else {
//         if ($scope.IsVisible == true) {
//           $scope.shipIds.push($scope.libShipId);
//           //if($scope.shipIds!=undefined &&$scope.groupName!=undefined &&group.emailId!=undefined){
//           if ($scope.sessionObject.roleId != 3) {
//             angular.forEach(group.vesselSelectedList, function (infos) {
//               if ($scope.libShipId != infos.id) {
//                 $scope.shipIds.push(infos.id);
//               }
//             });
//           }
//           var groupData = {
//             userProfileId: $scope.sessionObject.userId,
//             groupName: $scope.groupName,
//             shipIds: $scope.shipIds,
//             emailId: group.emailId,
//             mode: 'Email',
//             keyword: $scope.keyword,
//             documentHolderIds: $scope.selectedDocumentHolderIds,
//             loginId: $scope.sessionObject.userId,
//           };

//           FunctionalityService.shareExpiryDoc(JSON.stringify(groupData)).then(
//             function (response) {
//               $scope.loader = false;

//               if (response.status == 200) {
//                 $('#sharepopup').modal('hide');
//                 $state.reload();
//                 $timeout(function () {
//                   toaster.clear();
//                   toaster.success({ title: response.data.message });
//                   $scope.expDocUrl = '';
//                   angular.forEach(
//                     $scope.selctedcheckboxlst,
//                     function (value, key) {
//                       $scope.expDocUrl =
//                         $scope.expDocUrl +
//                         '\n' +
//                         ('\n' +
//                           value.documentHolderName +
//                           ': \n' +
//                           value.documentDownloadUrl +
//                           '\n');
//                     }
//                   );
//                   var mailToLink = group.emailId;
//                   $scope.url =
//                     'mailto:' +
//                     mailToLink +
//                     '?subject=' +
//                     $scope.libshipName +
//                     '%20Documents' +
//                     '&body=%0D%0A%0D%0A Below are the list of document attached.%0D%0A%0D%0A' +
//                     $window.encodeURIComponent($scope.expDocUrl) +
//                     '%0D%0A%0D%0A%0D%0A%0D%0A%0D%0ANote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//                   window.location.href = $scope.url;
//                   $scope.expDocUrlshare =
//                     $scope.expDocUrl +
//                     '\n\nNote: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.  Check your e-mail security settings to determine how attachments are handled.';
//                 }, 1000);
//               } else {
//                 toaster.clear();
//                 toaster.error({ title: response.data.message });
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//           //  }
//           //   else{
//           //     $scope.expDocUrl = "";
//           //     angular.forEach($scope.selctedcheckboxlst, function (value, key) {
//           //         $scope.expDocUrl = $scope.expDocUrl + ("\n" + value.documentHolderName + ": \n" + value.documentDownloadUrl + "\n");
//           //     });
//           //     var mailToLink = $scope.groupSearch;
//           //     $scope.url = 'mailto:' + mailToLink + '?subject=' + 'Share Expiry Document' + '&body=HI%20Dear/Madam,%20' + $window.encodeURIComponent($scope.expDocUrl)
//           //     window.location.href = $scope.url;
//           //   }
//         }
//       }
//       $scope.loader = false;
//       $scope.shareExpDoc = [];
//       $scope.oldAndNewExpDoc = [];
//       $scope.documentHolderIds = [];
//       //$scope.selctedcheckboxlst = [];
//     };

//     $scope.setPage = function (pageNo) {
//       $scope.currentPage = pageNo;
//     };

//     $scope.pageChanged = function () {
//       console.log('Page changed to: ' + $scope.currentPage);
//     };

//     $scope.setItemsPerPage = function (num) {
//       $scope.itemsPerPage = num;
//       $scope.currentPage = 1; //reset to first page
//     };
//     $scope.dateConversionForExpiry = function (day) {
//       var stringDate1 = day;
//       var splitDate1 = stringDate1.split('-');
//       var day1 = splitDate1[0];

//       var month1 = splitDate1[1];
//       var year1 = splitDate1[2];
//       this.dStartDate = month1 + '-' + day1 + '-' + year1;
//       return this.dStartDate;
//     };
//     $rootScope.documentTypeFilter = function (status) {
//       $scope.fielterFailure = 1;
//       $scope.selectStatus = status;
//       $scope.expiryDocumentHolderListFileter = [];
//       console.log('status', status);
//       var today = new Date();
//       var renewalDate = new Date(new Date().setDate(today.getDate() + 31));
//       var expiryDate;
//       if (status == 'Active') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (
//             expiry.expiryDateString != undefined ||
//             expiry.expiryDateString != ''
//           ) {
//             console.log('status1');

//             expiryDate = new Date(
//               $scope.dateConversionForExpiry(expiry.expiryDateString)
//             );
//             //expDate =  $scope.dateConversionForExpiry(expDate);
//           }
//           if (expiryDate > renewalDate || expiry.expiryDateString == '') {
//             console.log('status2');
//             $scope.expiryDocumentHolderListFileter.push(expiry);
//           }
//           console.info(
//             '$scope.expiryDocumentHolderListFileter inside',
//             $scope.expiryDocumentHolderListFileter
//           );
//         });
//       } else if (status == 'Renewal') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (
//             expiry.expiryDateString != undefined ||
//             expiry.expiryDateString != ''
//           ) {
//             expDate = $scope.dateConversionForExpiry(expiry.expiryDateString);
//             // console.log("renuven",expDate >= date2 && Renewal >= expDate,date2, Renewal,expDate)

//             expiryDate = new Date(
//               $scope.dateConversionForExpiry(expiry.expiryDateString)
//             );

//             console.log('date..>>>>>>...', today, renewalDate, expiryDate);
//             // if (expDate >= date2 && Renewal >= expDate )
//             if (today <= expiryDate && renewalDate >= expiryDate)
//               $scope.expiryDocumentHolderListFileter.push(expiry);
//           }
//         });
//       } else if (status == 'Expired') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           console.log(typeof expiry.expiryDateString);
//           if (
//             expiry.expiryDateString != undefined ||
//             expiry.expiryDateString != ''
//           )
//             expiryDate = new Date(
//               $scope.dateConversionForExpiry(expiry.expiryDateString)
//             );
//           if (expiryDate < today)
//             $scope.expiryDocumentHolderListFileter.push(expiry);
//         });
//       } else if (status == 'Missing') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (expiry.statusColor == undefined) {
//             $scope.expiryDocumentHolderListFileter.push(expiry);
//           }
//         });
//       } else {
//         $scope.expiryDocumentHolderListFileter =
//           $scope.expiryDocumentHolderList;
//       }
//       if ($scope.expiryDocumentHolderListFileter.length == 0) {
//         $scope.fielterFailure = 0;
//       }
//     };

//     $scope.$on('$viewContentLoaded', function () {
//       FunctionalityService.getDashboardTopCountBasedOnVessel(
//         $scope.libShipId
//       ).then(
//         function (response) {
//           if (response.status == 200) {
//             console.log('ship based count::' + JSON.stringify(response.data));
//             $scope.headerDetails = response.data.usershipCount;
//             console.log(
//               'headerDetails in vessel3::' +
//                 JSON.stringify($scope.headerDetails)
//             );
//           }
//         },
//         function (error) {
//           console.log('message :: ' + error);
//         }
//       );
//     });
//     $scope.logout = function () {
//       $window.localStorage.removeItem('sessionObject');
//       $window.localStorage.removeItem('userRole');
//       $window.localStorage.removeItem('userName');
//       $window.localStorage.removeItem('userEmail');
//       $window.localStorage.removeItem('userId');
//       $window.localStorage.removeItem('roleId');
//       $window.localStorage.removeItem('role');
//       $window.localStorage.removeItem('organizationId');
//       $window.localStorage.removeItem('organizationName');
//       $window.localStorage.removeItem('profilePicture');
//       $window.localStorage.removeItem('maxShipCount');
//       $window.localStorage.removeItem('maxUserCount');
//       $window.localStorage.removeItem('shipProfileInfos');
//       $window.localStorage.removeItem('groupShipId');
//       $window.localStorage.removeItem('groupShipName');
//       $window.localStorage.removeItem('editId');
//       $window.localStorage.removeItem('countryName');
//       $window.localStorage.removeItem('stateName');
//       $window.localStorage.removeItem('shipId');
//       $window.localStorage.removeItem('libShipId');
//       $window.localStorage.removeItem('libshipName');
//       localStorage.removeItem('logout-event');
//       $window.localStorage.removeItem('logoPicture');

//       toaster.pop('success', 'Logout succesfully');
//       setTimeout(function () {
//         $location.path('/');
//       }, 2000);
//       $window.location.reload();
//     };
//   },
// ]);

// //Directive for File Upload
// userVesselDocEBD.directive('fileModel', [
//   '$parse',
//   function ($parse) {
//     return {
//       restrict: 'A',
//       link: function (scope, element, attrs) {
//         var model = $parse(attrs.fileModel);
//         var modelSetter = model.assign;

//         element.bind('change', function () {
//           scope.$apply(function () {
//             modelSetter(scope, element[0].files[0]);
//           });
//         });
//       },
//     };
//   },
// ]);
// userVesselDocEBD.directive('customFocus', [
//   function () {
//     var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
//     return {
//       restrict: 'A', //Angular will only match the directive against attribute names
//       require: 'ngModel',
//       link: function (scope, element, attrs, ctrl) {
//         ctrl.$focused = false;

//         element
//           .bind('focus', function (evt) {
//             element.addClass(FOCUS_CLASS);
//             scope.$apply(function () {
//               ctrl.$focused = true;
//             });
//           })
//           .bind('blur', function (evt) {
//             element.removeClass(FOCUS_CLASS);
//             scope.$apply(function () {
//               ctrl.$focused = false;
//             });
//           });
//       },
//     };
//   },
// ]);

var userVesselDocEBD = angular.module('dapp.UserVesselDocumentEBDController', [
  'ui.select',
  'angularUtils.directives.dirPagination',
  'toaster',
  'moment-picker',
  '720kb.tooltips',
  'ngSanitize',
]);

userVesselDocEBD.controller('UserVesselDocumentEBDController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  '$timeout',
  'toaster',
  '$filter',
  '$sce',
  'FunctionalityService',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    $timeout,
    toaster,
    $filter,
    $sce,
    FunctionalityService
  ) {
    // --- INITIALIZATIONS (Prevents Blank Screen Crashes) ---
    $scope.expiryDocumentHolderList = [];
    $scope.expiryDocumentHolderListFilter = []; // Fixed Spelling
    $scope.notificationList = []; // Added to prevent repeat crash
    $scope.vesselList = [];
    $scope.selctedExpiredDocumentList = [];
    $scope.selectedDocumentHolderIds = [];
    $scope.searchEBD = ''; // Initialize filter string
    $scope.itemsPerPage = 10; // Default pagination size
    $scope.currentPage = 1;
    $scope.loader = false;

    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject') || '{}'
    );
    $scope.libShipId = '';
    $scope.libshipName = '';
    $scope.group = {};
    $scope.vesselSelectedList = [];
    $scope.formattedIssueDate = '';
    $scope.formattedExpiryDate = '';
    $scope.formattedlastAnnual = '';
    $scope.formattednextAnnual = '';
    $scope.issuingAuthority = '';
    $scope.notfound = true;
    $scope.viewby = 73;
    $scope.roleIdForView = $scope.sessionObject.roleId;
    $scope.itemsPerPageForHistory = $scope.viewby;
    $scope.activeText = 'Active';

    if ($scope.sessionObject.roleId != 3) {
      $scope.libShipId = $window.localStorage.getItem('libShipId');
      $scope.libshipName = $window.localStorage.getItem('libshipName');
      $scope.libshipNameDoc = $scope.libshipName + ' ' + 'Documents';
    } else if (
      $scope.sessionObject.shipProfileInfos &&
      $scope.sessionObject.shipProfileInfos.length > 0
    ) {
      $scope.libShipId = $scope.sessionObject.shipProfileInfos[0].id;
      $scope.libshipName = $scope.sessionObject.shipProfileInfos[0].shipName;
    }

    // --- API DATA FETCHING ---
    $scope.getAllExpiryList = function () {
      $scope.loader = true;
      var archivedStatus = 0;
      FunctionalityService.getAllExpiryDocumentList(
        $scope.libShipId,
        archivedStatus
      ).then(
        function (response) {
          if (response.status == 200 || response.status == 201) {
            $scope.expiryDocumentHolderList =
              response.data.expiryDocumentList || [];
            // FIX: Using corrected spelling 'Filter'
            $scope.expiryDocumentHolderListFilter =
              $scope.expiryDocumentHolderList;
            $scope.expiryDocumentHolderListLen =
              $scope.expiryDocumentHolderList.length;
          }
          $scope.loader = false;
        },
        function myError(err) {
          $scope.loader = false;
          $scope.records = $scope.expiryDocumentHolderList.length == 0;
          console.error('Error response', err);
        }
      );

      var data1 = {
        userId: $scope.sessionObject.userId,
        roleId: $scope.sessionObject.roleId,
      };
      FunctionalityService.getVesselProfileList(data1).then(
        function (response) {
          if (response.status == 200) {
            angular.forEach(response.data.shipProfileList, function (val) {
              if (val.id != $scope.libShipId) $scope.vesselList.push(val);
            });
          }
        }
      );
    };

    // --- DOCUMENT HANDLING ---
    $scope.uploadFile = function () {
      toaster.clear();
      $rootScope.convertedFile = '';
      if ($scope.myFile != undefined) {
        $scope.loader = true;
        if ($scope.myFile.size < 20971520) {
          $rootScope.uploadDocFile = $scope.myFile;
          // FIX: Trust local URL for preview
          $rootScope.convertedFile = $sce.trustAsResourceUrl(
            URL.createObjectURL($scope.myFile)
          );

          FunctionalityService.scanExpiryDocument(
            $rootScope.uploadDocFile
          ).then(function (response) {
            $scope.loader = false;
            if (response.status == 200 || response.status == 201) {
              $('#fileupload').modal('hide');
              if (response.data.expiryDocumentDTOs) {
                $scope.libcreate = response.data.expiryDocumentDTOs;
              }
              $('#createEBD').modal('toggle');
            }
          });
        } else {
          toaster.info({ title: 'File too large (Max 20MB)' });
        }
      }
    };

    $scope.viewExpiryDocumentInformation = function (documentObj) {
      $timeout(function () {
        $('#viewExpiryDocument').modal('show');
        $scope.viewDocumentObjectInfo = documentObj;
        $scope.docId = documentObj.id;

        // FIX: Securely trust the External URL
        $scope.viewDocumentUrl = $sce.trustAsResourceUrl(
          documentObj.documentPreviewUrl
        );

        // Force iframe update
        var pdfViewerEmbed = document.getElementById('embedContainer');
        if (pdfViewerEmbed) {
          pdfViewerEmbed.setAttribute('src', $scope.viewDocumentUrl);
        }

        $scope.certificateNumberEdit = documentObj.certificateNumber;
        $scope.issuingAuthorityEdit = documentObj.issuingAuthority;
        $scope.placeOfIssueEdit = documentObj.placeOfIssue;
        $scope.issueDateStringEdit = documentObj.issueDateString;
        $scope.expiryDateStringEdit = documentObj.expiryDateString;
        $scope.lastAnnualStringEdit = documentObj.lastAnnualString;
        $scope.nextAnnualStringEdit = documentObj.nextAnnualString;
        $scope.remarksEdit = documentObj.remarks;
      }, 500);
    };

    // --- SELECTION LOGIC (Safe from undefined) ---
    $scope.checkUncheckAll = function () {
      $scope.selctedExpiredDocumentList = [];
      $scope.selectedDocumentHolderIds = [];

      // Safety: Use (list || [])
      var list = $scope.expiryDocumentHolderListFilter || [];

      angular.forEach(list, function (item) {
        item.checked = $scope.checkall;
        if (item.checked && item.id != undefined) {
          $scope.selctedExpiredDocumentList.push(item);
          $scope.selectedDocumentHolderIds.push(item.documentHolderId);
        }
      });
    };

    // --- FILTERING LOGIC (Fixed Typo Version) ---
    $rootScope.documentTypeFilter = function (status) {
      $scope.fielterFailure = 1;
      $scope.selectStatus = status;
      $scope.expiryDocumentHolderListFilter = []; // Fixed Spelling

      var today = new Date();
      var renewalThreshold = new Date(new Date().setDate(today.getDate() + 31));

      angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
        if (!expiry.expiryDateString) {
          if (status == 'Missing' || status == 'All')
            $scope.expiryDocumentHolderListFilter.push(expiry);
          return;
        }

        var expiryDate = new Date(
          $scope.dateConversionForExpiry(expiry.expiryDateString)
        );

        if (status == 'Active' && expiryDate > renewalThreshold) {
          $scope.expiryDocumentHolderListFilter.push(expiry);
        } else if (
          status == 'Renewal' &&
          today <= expiryDate &&
          renewalThreshold >= expiryDate
        ) {
          $scope.expiryDocumentHolderListFilter.push(expiry);
        } else if (status == 'Expired' && expiryDate < today) {
          $scope.expiryDocumentHolderListFilter.push(expiry);
        } else if (status == 'All') {
          $scope.expiryDocumentHolderListFilter =
            $scope.expiryDocumentHolderList;
        }
      });

      if ($scope.expiryDocumentHolderListFilter.length == 0)
        $scope.fielterFailure = 0;
    };

    // --- HELPERS ---
    $scope.dateConversionForExpiry = function (day) {
      if (!day) return '';
      var parts = day.split('-');
      return parts[1] + '-' + parts[0] + '-' + parts[2]; // Converts DD-MM-YYYY to MM-DD-YYYY
    };

    $scope.logout = function () {
      $window.localStorage.clear();
      toaster.pop('success', 'Logout successfully');
      $timeout(function () {
        $location.path('/');
      }, 1000);
    };
  },
]);

// --- DIRECTIVES ---
userVesselDocEBD.directive('fileModel', [
  '$parse',
  function ($parse) {
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
      },
    };
  },
]);
