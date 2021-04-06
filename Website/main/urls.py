from django.contrib import admin
from django.urls import path,include
from django.conf import settings 
from django.conf.urls.static import static 
from . import views

urlpatterns = [
    path('', views.login, name='login'),
    path('info', views.info, name='info'),
    path('admin', views.admin, name='admin'),
    path('researcher', views.researcher, name='researcher'),
    path('forest_employee', views.forest_employee, name='forest_employee'),
    path('task', views.task, name='task'),
    path('addtask', views.addtask, name='addtask'),
    path('assigntask', views.assigntask, name='assigntask'),
    path('addanimal', views.addanimal, name='addanimal'),
    path('addcamera', views.addcamera, name='addcamera'),
    path('addresearcher', views.addresearcher, name='addresearcher'),
    path('addforest_employee', views.addforest_employee, name='addforest_employee'),
    path('researcherlist', views.researcherlist, name='researcherlist'),
    path('location',views.location,name='location'),
    path('geojson',views.geojson,name='geojson'),
    path('editresearcher/<id>/',views.editresearcher,name='editresearcher'),
    path('report',views.report,name='report'),
    path('stats',views.stats,name='stats'),
    path('reportlist',views.reportlist,name='reportlist'),
    path('localreportlist',views.localreportlist,name='localreportlist'),
    path('task_description',views.task_description,name='task_description'),
    path('alertmap',views.alertmap,name='alertmap'),
    path('getexcelanimal',views.getexcelanimal,name='getexcelanimal'),
    path('allotrange',views.allotrange,name='allotrange'),
    path('allotbeat',views.allotbeat,name='allotbeat'),
    path('track',views.track,name='track'),
    #######API#####
    # path('gettask', views.give_task.as_view(), name='appdata'),
    # path('apptask', views.manage_task.as_view(), name='appdata'),
    path('applogin', views.manage_login.as_view(), name='applogin'),
    path('alert',views.alert.as_view(),name='alert'),
    path('backtask',views.backtask.as_view(),name='backtask'),
]