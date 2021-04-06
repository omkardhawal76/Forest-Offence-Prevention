from django.shortcuts import render,redirect
from django.http import JsonResponse
from .models import *
from .forms import *
from background_task import background
import time
import subprocess
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import pusher
from tensorflow import keras
import numpy as np
import datetime
import threading
import xlsxwriter
# import StringIO

# Use a service account

###########API##############
# from .models import Snippet
# from .serializers import Task_serializer
from django.http import Http404, HttpResponse
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import json

def login(request):
    # fun()
    # back()
    # with open('/home/ravi/python projects/sih2020/server/app/process.sh', 'rb') as file:
    #     script = file.read()
    # rc = subprocess.call(script, shell=True)
    request.session.flush()
    # process = subprocess.Popen(['python', 'manage.py','process_tasks'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    return render(request, "login.html", {})

def info(request):
    if request.method == 'POST':    
        user = request.POST.get('username')
        passw = request.POST.get('password')
        print(user,passw)
        if user =='admin' and passw=='admin123':
            request.session['data']={'area':"a-admin"}
            return redirect('admin')
        else:
            try:
                data=Researcher.objects.get(username=user,password=passw)
                flag="r"
            except Researcher.DoesNotExist:
                try:
                    data=Forest_employee.objects.get(username=user,password=passw)
                    flag="f"
                except:
                    data = None
                    flag = None
            # data=Researcher.objects.get(username=user,password=passw)
            print(data,flag)
            if data==None:
                return redirect('login')
            if flag=="r":
                request.session['data']={'researcher_id':data.researcher_id}
                request.session['flag']="r"
                return redirect('researcher')
            elif flag=="f":
                request.session['data']={'area':data.area,'empid':data.empid}
                # if data.role=='employee':
                #     request.session['flag']="e"
                # else:
                #     request.session['flag']="f"
                request.session['flag']=data.role
                return redirect('forest_employee')
    # return render(request, "next.html", {})

def admin(request):
    ##ml model
    model=keras.models.load_model('main/static/hood_2')
    test=np.array([2020,197]).reshape(1,2)
    pred=model.predict(test)
    val=[]
    with open('main/static/Neighbourhoods.geojson', 'r') as openfile: 
  
    # Reading from json file 
        data = json.load(openfile) 
    # print(pred)
    for i in pred:
        # print(i)
        val=i
        ##x>=0.5 red,0.5>x>=0.2 yellow,x<0.2 green
    for i in range(len(val)):
        if val[i]>=0.5:
            data['features'][i]['properties']["AREA_SHORT_CODE"]=1
        elif val[i]<0.5 and val[i]>=0.2:
            data['features'][i]['properties']["AREA_SHORT_CODE"]=2
        else:
            data['features'][i]['properties']["AREA_SHORT_CODE"]=3
    # data['features']=sorted(data['features'], key=lambda k: k['properties'].get("AREA_SHORT_CODE",0))
    with open('main/static/Neighbourhoods.geojson', "w") as outfile: 
        json.dump(data, outfile) 

    animals=[]
    sdict={}
    slat={}
    slon={}
    x=Animal.objects.all()
    for y in x:
        animals.append(y.animal_info)
    animals=list(set(animals))
    for atype in animals:
        sdict[atype]=[]
        slat[atype]=[]
        slon[atype]=[]
        a=Animal.objects.filter(animal_info=atype)
        for i in a:
            sdict[atype].append(i.animal_id)
            slat[atype].append(i.latitude[-1])
            slon[atype].append(i.longitude[-1])
    print(sdict,slat,slon)
    return render(request,"admin.html",{"animals":animals,"sdict":sdict,"slat":slat,"slon":slon})
    
def researcher(request):
    data=request.session.get('data')
    print(data)
    r=Researcher.objects.get(researcher_id=data['researcher_id'])
    animals=r.animal
    print(animals)
    sdict={}
    slat={}
    slon={}
    for atype in animals:
        sdict[atype]=[]
        slat[atype]=[]
        slon[atype]=[]
        a=Animal.objects.filter(animal_info=atype)
        for i in a:
            sdict[atype].append(i.animal_id)
            slat[atype].append(i.latitude[-1])
            slon[atype].append(i.longitude[-1])
    print(sdict,slat,slon)
    return render(request,"researcher.html",{"animals":animals,"sdict":sdict,"slat":slat,"slon":slon})

def forest_employee(request):
    data=request.session.get('data')
    print(data)
    animals=[]
    sdict={}
    slat={}
    slon={}
    x=Animal.objects.all()
    for y in x:
        animals.append(y.animal_info)
    animals=list(set(animals))
    for atype in animals:
        sdict[atype]=[]
        slat[atype]=[]
        slon[atype]=[]
        a=Animal.objects.filter(animal_info=atype)
        for i in a:
            sdict[atype].append(i.animal_id)
            slat[atype].append(i.latitude[-1])
            slon[atype].append(i.longitude[-1])
    print(sdict,slat,slon)
    return render(request,"forest_employee.html",{"animals":animals,"sdict":sdict,"slat":slat,"slon":slon})
    
def task(request):
    for key, value in request.session.items():
        print('{} => {}'.format(key, value))
    flag=request.session.get("flag")
    if flag=="r":
        data=Tasks.objects.filter(task_from=request.session.get('data')['researcher_id'])
        return render(request,"task.html",{'data':data,'flag':flag})
    else:
        # fn=request.session.get('data')['forest_name']
        # data=[]
        # d=Tasks.objects.filter(task_to=fn)
        # s=Tasks.objects.filter(task_from=fn)
        # print(s,d)
        # for i in d:
        #     data.append(i)
        # print(data,fn)
        # for i in s:
        #     for j in range(len(data)):
        #         # print(data[j].task_id)
        #         if data[j]!=-1:
        #             if i.task_id==data[j].task_id:
        #                 data[j]=-1
        #     data.append(i)
        # data=list(filter(lambda a: a != -1, data))

        # w=Forest_employee.objects.filter(forest_name=fn)
        # print(w)
        # ind=0
        # for i in w:
        #     i.empid
        # print(flag)
        role=request.session.get('flag')
        info=request.session.get('data')
        if role == 'division_incharge':
            area=info['area']
            w=Division_range.objects.filter(division_id=area)
            data=[]
            d1=Division_tasks.objects.filter(task_to=area)
            d2=Division_tasks.objects.filter(task_from=area)
            for i in d1:
                data.append(i)
            for i in d2:
                data.append(i)
            # for i in d2:
            #     for j in range(len(data)):
            #         # print(data[j].task_id)
            #         if data[j]!=-1:
            #             if i.task_id==data[j].task_id:
            #                 data[j]=-1
            #     data.append(i)
            # data=list(filter(lambda a: a != -1, data))
        elif role == 'range_incharge':
            area=info['area']
            lb=Range_beat.objects.filter(range_id=area)
            w=[]
            d1=Range_tasks.objects.filter(task_to=area)
            d2=Range_tasks.objects.filter(task_from=area)
            data=[]
            for i in d1:
                data.append(i)
            for i in d2:
                data.append(i)
            # for i in d2:
            #     for j in range(len(data)):
            #         # print(data[j].task_id)
            #         if data[j]!=-1:
            #             if i.task_id==data[j].task_id:
            #                 data[j]=-1
            #     data.append(i)
            # data=list(filter(lambda a: a != -1, data))
            for i in lb:
                beat_id=i.beat_id
                w.append(Forest_employee.objects.get(area=beat_id))
        elif role == 'beat_incharge':
            area=info['area']
            data=Range_tasks.objects.filter(task_to=area)
            w=[]
        flag=role
    #data= list of tasks ,flag=role ,workers=to whom to assign
    print(data,flag,w)
    return render(request,"task.html",{'data':data,'flag':flag,"workers":w})# to be continued from forest officer

def addtask(request):
    for key, value in request.session.items():
        print('{} => {}'.format(key, value))
    if request.method == 'POST':
        print(request.POST)
        t=Tasks()
        count=Tasks.objects.count()
        t.task_id='id_'+(str(count+1))
        t.task_from=request.session.get('data')['researcher_id']
        t.task_info=request.POST.get("task_info")
        t.task_to=request.POST.get("task_to")
        t.status='incomplete'
        t.deadline=request.POST.get("deadline")
        print(t)
        t.save()

        t=Division_tasks()
        count=Tasks.objects.count()
        t.task_id='id_'+(str(count+1))
        t.task_from=request.session.get('data')['researcher_id']
        t.task_info=request.POST.get("task_info")
        t.task_to=request.POST.get("task_to")
        t.status='incomplete'
        t.deadline=request.POST.get("deadline")
        print(t)
        t.save()
        # form = addtaskform(request.POST or None)
        # if form.is_valid():
        #     form.save()  
        return render(request,"done.html",{})
        # else:
        #     # form.errors.as_data()
        #     print('error',form.errors.as_data())
    division=[]
    data=Division_range.objects.all()
    for i in data:
        division.append(i.division_id)
    division=list(set(division))
    return render(request,"addtask.html",{'division':division})

def assigntask(request):
    # for key, value in request.session.items():
    #     print('{} => {}'.format(key, value))
    if request.method == 'POST':
        print(request.POST)
        x=request.POST['task_to'].split('-')
        if x[0]!='r' and x[0]!='d':
            task=Range_tasks.objects.get(task_id=request.POST['task_id'])
            Range_tasks.objects.filter(task_id=request.POST["task_id"]).update(status="assigned")
            # Range_tasks.objects.filter(task_id=request.POST["task_id"]).update(task_to=request.POST['task_to'])
            ta={}
            ta["task_id"]=task.task_id
            ta["task_from"]=task.task_to
            ta["task_info"]=task.task_info
            ta["task_to"]=request.POST['task_to']
            ta["status"]='assigned'
            ta["deadline"]=task.deadline.strftime('%Y-%m-%d')
            print(ta)

            t=Range_tasks()
            t.task_id=task.task_id
            t.task_from=task.task_to
            t.task_info=task.task_info
            t.task_to=request.POST['task_to']
            t.status='assigned'
            t.deadline=task.deadline.strftime('%Y-%m-%d')
            t.save()
            # t.save()
            if not firebase_admin._apps:
                data = open('main/static/serviceAccount.json').read() #opens the json file and saves the raw contents
                jsonData = json.loads(data) #converts to a json structure

                cred = credentials.Certificate(jsonData)
                firebase_admin.initialize_app(cred)
                db = firestore.client()
            db = firestore.client()
            doc_ref = db.collection(u'task').document(u'assign')
            temp=doc_ref.get()
            temp=temp.to_dict()
            print(temp)
            if request.POST['task_to'] in temp:
                temp[request.POST['task_to']].append(ta)
            else:
                temp[request.POST['task_to']]=[ta]
            print(temp)
            doc_ref.set(temp)
        elif x[0]=='r':
            task=Division_tasks.objects.get(task_id=request.POST['task_id'])
            Division_tasks.objects.filter(task_id=request.POST["task_id"]).update(status="assigned")
            Division_tasks.objects.filter(task_id=request.POST["task_id"]).update(task_to=request.POST['task_to'])
            Division_tasks.objects.filter(task_id=request.POST["task_id"]).update(task_from=task.task_to)
            t=Range_tasks()
            t.task_id=task.task_id
            t.task_from=task.task_to
            t.task_info=task.task_info
            t.task_to=request.POST['task_to']
            t.status='incomplete'
            t.deadline=task.deadline.strftime('%Y-%m-%d')
            t.save()
        # elif x[0]=='r':
        #     task=Range_tasks.objects.get(task_id=request.POST['task_id'])
        #     Range_tasks.objects.filter(task_id=request.POST["task_id"]).update(status="assigned")
        #     t=Tasks()
        #     t.task_id=task.task_id
        #     t.task_from=task.task_to
        #     t.task_info=task.task_info
        #     t.task_to=request.POST['task_to']
        #     t.status='assigned'
        #     t.deadline=task.deadline.strftime('%Y-%m-%d')
        #     t.save()
    return HttpResponse(status=200)

def task_description(request):
    tid=request.POST['task_id']
    print(tid)
    ts=Task_Description.objects.get(task_id=tid)
    data={ "task_id":ts.task_id,
            "image":"img",
            "description":ts.description}
    # print(bytes(ts.image))
    
    data['image']=bytes(ts.image).decode("utf-8") 
    
    return HttpResponse(status=200,content=str(json.dumps(data)))

def addanimal(request):
    if request.method == 'POST':
        print((request.POST['animal_id']))
        a=Animal()
        count=Animal.objects.count()
        # print(count)
        a.animal_id='id_'+(str(count+1))
        a.animal_name=request.POST['animal_name']
        a.animal_info=request.POST['animal_info']
        a.latitude=[0]
        a.longitude=[0]
        a.save()
        # form = addanimalform(request.POST or None)
        # if(False):
        # if form.is_valid():
        # form.save()
        #firebase#
        if not firebase_admin._apps:
            data = open('main/static/serviceAccount.json').read() #opens the json file and saves the raw contents
            jsonData = json.loads(data) #converts to a json structure

            cred = credentials.Certificate(jsonData)
            firebase_admin.initialize_app(cred)
        db = firestore.client()
        print(db,'id_'+(str(count+1)))

        map_alert=db.collection(u'map_alert').document('animal')
        tem=map_alert.get()
        if tem.exists:
            an=tem.to_dict()
            if request.POST['animal_info'] in an:
                an[request.POST['animal_info']].append('id_'+(str(count+1)))
            else:
                an[request.POST['animal_info']]=['id_'+(str(count+1))]
            map_alert.update(an)
            
        an_loc=db.collection(u'map_alert').document('location')
        tem=an_loc.get()
        if tem.exists:
            lo=tem.to_dict()
            lo['id_'+(str(count+1))]=[0,0]
            an_loc.update(lo)

        animal = db.collection(u'animals').document('id_'+(str(count+1)))
        animal.set({
            u'latitude': 0,
            u'longitude': 0,
        })

        animal = db.collection(u'animals_update').document('new')
        animal.set({
            u'animal_id': 'id_'+(str(count+1)),
            u'animal_type': request.POST['animal_info'],
        })

        ##animal list##
        animals_list = db.collection(u'animals_list').document(request.POST['animal_info'])
        temp=animals_list.get()
        if temp.exists:
            print("yes")
            sel=temp.to_dict()
            print(sel['id'])
            tem=sel['id']
            tem.append('id_'+(str(count+1)))
            animals_list.update({'id':tem})
        else:
            print("no")
            animals_list.set({
                u'id':['id_'+(str(count+1))]
            })

        docs = db.collection(u'animals').stream()

        for doc in docs:
            print(f'{doc.id} => {doc.to_dict()}')
        #######
        
        return render(request,"done.html",{})  
    return render(request,"addanimal.html",{})

def addcamera(request):
    if request.method == 'POST':
        # form = addcameraform(request.POST or None)
        # if form.is_valid():
        #     form.save()  
        c=Camera()
        count=Camera.objects.count()
        c.camera_id='id_'+(str(count+1))
        c.latitude=request.POST['latitude']
        c.longitude=request.POST['longitude']
        c.status=request.POST['status']
        c.save()
        return render(request,"done.html",{})
    return render(request,"addcamera.html",{})

def addresearcher(request):
    if request.method == 'POST':
        # print(request.body)
        # form = addresearcherform(request.POST or None)
        r=Researcher()
        count=Researcher.objects.count()
        r.researcher_id='id_'+(str(count+1))
        r.researcher_name=request.POST['researcher_name']
        r.experience=request.POST['experience']
        r.qualification=request.POST['qualification']
        r.animal=request.POST['animal'].split(",")
        r.username=request.POST['username']
        r.password=request.POST['password']
        r.save()
        # if (False):
            # form.save() 
        return render(request,"done.html",{})
        # else:
        #     # form.errors.as_data()
        #     print('error',form.errors.as_data())
    data=Animal.objects.all()
    al=[]
    for i in data:
        al.append(i.animal_info)
    al=list(set(al))
    return render(request,"addresearcher.html",{"animal":al})

def addforest_employee(request):
    if request.method == 'POST':
        temp=Forest_employee()
        count=Forest_employee.objects.count()
        temp.empid='id_'+(str(count+1))
        temp.name=request.POST['name']
        temp.forest_name=request.POST['forest_name']
        temp.role=request.POST['role']
        temp.username=request.POST['username']
        temp.password=request.POST['password']
        temp.save()
        return render(request,"done.html",{})

    return render(request,"addforest_employee.html",{})

def researcherlist(request):
    data=Researcher.objects.all()
    return render(request,"researcherlist.html",{'data':data})

def location(request):
    print(request.POST['animals'])
    animals=request.POST['animals'].split(",")
    sdict={}
    slat={}
    slon={}
    for atype in animals:
        sdict[atype]=[]
        slat[atype]=[]
        slon[atype]=[]
        a=Animal.objects.filter(animal_info=atype)
        for i in a:
            sdict[atype].append(i.animal_id)
            slat[atype].append(i.latitude[-1])
            slon[atype].append(i.longitude[-1])
    print(sdict,slat,slon)
    return JsonResponse({"sdict":sdict,"slat":slat,"slon":slon})
    # return HttpResponse({"sdict":sdict,"slat":slat,"slon":slon},content_type="application/json")

def geojson(request):
    data = open('main/static/Neighbourhoods.geojson').read() #opens the json file and saves the raw contents
    data = json.loads(data)
    # data['features']=sorted(data['features'], key=lambda k: k['properties'].get("AREA_SHORT_CODE",0))
    return JsonResponse(data, safe=False)

def editresearcher(request,id="0"):
    print(id)
    if request.method == 'POST':
        d=Researcher.objects.get(researcher_id=id).delete()
        r=Researcher()
        r.researcher_id=id
        r.researcher_name=request.POST['researcher_name']
        r.experience=request.POST['experience']
        r.qualification=request.POST['qualification']
        r.animal=request.POST['animal'].split(",")
        r.username=request.POST['username']
        r.password=request.POST['password']
        r.save()
        # if (False):
            # form.save() 
        return render(request,"done.html",{})
    r=Researcher.objects.get(researcher_id=id)
    ra=set(r.animal)
    data=Animal.objects.all()
    al=[]
    for i in data:
        if i.animal_info not in ra: 
            al.append(i.animal_info)
    al=list(set(al))
    ra=list(ra)
    resa=ra[0]
    for i in range(1,len(ra)):
        resa+=","+ra[i]
    return render(request,"editresearcher.html",{"r":r,"animal":al,"resa":resa})

def report(request):
    return render(request,"report.html",{})

def reportlist(request):
    main_dict={}
    # rep=Report.objects.all()
    data=request.session.get('data')
    print(data)
    work_id=data['area'].split("-")
    work_id=work_id[0]
    print(work_id)
    if work_id=='a':
        ldivision=Division_range.objects.all()
        for x in ldivision:
            tem_dict={}
            lrange=Division_range.objects.filter(division_id=x.division_id)
            for i in lrange:
                range_id=i.range_id
                lbeat=Range_beat.objects.filter(range_id=range_id)
                tem_dict[range_id]=[]
                for j in lbeat:
                    empid=j.beat_id
                    empid=Beat_employee.objects.get(beat_id=beat).empid
                    emp=Forest_employee.objects.get(empid=empid)
                    reports=Report.objects.filter(empid=(emp.empid+'-'+emp.name))
                    tem_dict[range_id]={}
                    for k in reports:
                        rep_dic={}
                        rep_dic['empid']=k.empid
                        rep_dic['description']=k.description
                        rep_dic['image']=bytes(k.image).decode("utf-8") 
                        rep_dic['latitude']=k.latitude
                        rep_dic['longitude']=k.longitude
                        if k.empid in tem_dict[range_id]:
                            tem_dict[range_id][k.empid].append(rep_dic)
                        else:
                            tem_dict[range_id][k.empid]=[]
                            tem_dict[range_id][k.empid].append(rep_dic)
            main_dict[x.division_id]=tem_dict
    elif work_id=='d':
        lrange=Division_range.objects.filter(division_id=data['area'])
        for i in lrange:
            range_id=i.range_id
            print(range_id)
            lbeat=Range_beat.objects.filter(range_id=range_id)
            main_dict[range_id]={}
            for j in lbeat:
                beat=j.beat_id
                empid=Beat_employee.objects.get(beat_id=beat).empid
                emp=Forest_employee.objects.get(empid=empid)
                print(emp.empid+emp.name)
                reports=Report.objects.filter(empid=(emp.empid+'-'+emp.name))
                # main_dict[range_id]={}
                for k in reports:
                    rep_dic={}
                    rep_dic['empid']=k.empid
                    rep_dic['description']=k.description
                    rep_dic['image']=bytes(k.image).decode("utf-8") 
                    rep_dic['latitude']=k.latitude
                    rep_dic['longitude']=k.longitude
                    if k.empid in main_dict[range_id]:
                        main_dict[range_id][k.empid].append(rep_dic)
                    else:
                        main_dict[range_id][k.empid]=[]
                        main_dict[range_id][k.empid].append(rep_dic)
    elif work_id=='r':
        range_id=data['area']
        lbeat=Range_beat.objects.filter(range_id=range_id)
        main_dict[range_id]=[]
        for j in lbeat:
            empid=j.beat_id
            empid=Beat_employee.objects.get(beat_id=beat).empid
            emp=Forest_employee.objects.get(empid=empid)
            reports=Report.objects.filter(empid=(emp.empid+'-'+emp.name))
            main_dict[range_id]={}
            for k in reports:
                rep_dic={}
                rep_dic['empid']=k.empid
                rep_dic['description']=k.description
                rep_dic['image']=bytes(k.image).decode("utf-8") 
                rep_dic['latitude']=k.latitude
                rep_dic['longitude']=k.longitude
                if k.empid in main_dict[range_id]:
                    main_dict[range_id][k.empid].append(rep_dic)
                else:
                    main_dict[range_id][k.empid]=[]
                    main_dict[range_id][k.empid].append(rep_dic)
    elif work_id=='b':
        empid=data['area']
        empid=Beat_employee.objects.get(beat_id=beat).empid
        emp=Forest_employee.objects.get(empid=empid)
        reports=Report.objects.filter(empid=(emp.empid+'-'+emp.name))
        main_dict[work_id]=[]
        for k in reports:
            rep_dic={}
            rep_dic['empid']=k.empid
            rep_dic['description']=k.description
            rep_dic['image']=bytes(k.image).decode("utf-8") 
            rep_dic['latitude']=k.latitude
            rep_dic['longitude']=k.longitude
            main_dict[work_id].append(rep_dic)
    
            #sort the array
    # print(main_dict)
    return render(request,"reportlist.html",{"main_dict":main_dict,"flag":work_id})

def localreportlist(request):
    r=Local_report.objects.all()
    main_dict=[]
    for lr in r:
            rep_dic={}
            rep_dic['phone']=lr.phone_no
            rep_dic['description']=lr.description
            rep_dic['image']=bytes(lr.image).decode("utf-8") 
            rep_dic['latitude']=lr.latitude
            rep_dic['longitude']=lr.longitude
            main_dict.append(rep_dic)
    return render(request,"localreportlist.html",{"main_dict":main_dict})

def alertmap(request):
    return render(request,"alertmap.html",{})

def track(request):
    for key, value in request.session.items():
        print('{} => {}'.format(key, value))
    if request.session['flag']=='division_incharge':
        div=request.session['data']['area']
        ran=Division_range.objects.filter(division_id=div)
        beat=[]
        arr=[]
        data={}
        for i in ran:
            b=Range_beat.objects.filter(range_id=i.range_id)
            for j in b:
                beat.append(j.beat_id)
                arr.append(str(i.range_id)+"->"+str(j.beat_id))
                print(j.beat_id)
                f=Beat_employee.objects.get(beat_id=j.beat_id)
                print(f.empid)
                emp=Forest_employee.objects.get(empid=f.empid)
                print(emp.empid)
                if j.beat_id in data:
                    for k in range(len(emp.latitude)):
                        temp=[]
                        temp.append(emp.longitude[k])
                        temp.append(emp.latitude[k])
                        data[j.beat_id].append(temp)
                else:
                    data[j.beat_id]=[]
                    for k in range(len(emp.latitude)):
                        temp=[]
                        temp.append(emp.longitude[k])
                        temp.append(emp.latitude[k])
                        data[j.beat_id].append(temp)
                
    print(data)
    return render(request,"track.html",{'data':data,'arr':arr})

def stats(request):
    return render(request,"dashboard.html",{})

def allotrange(request):
    div_ran={}
    d=Division_range.objects.all()
    for i in d:
        div_ran[i.division_id]=i.range_id
    ran_beat={}
    r=Range_beat.objects.all()
    for i in r:
        ran_beat[i.range_id]=i.beat_id
    print(div_ran,ran_beat)
    return render(request,'allotrange.html',{'div_ran':div_ran,'ran_beat':ran_beat})

def allotbeat(request):
    ran_beat={}
    d=Range_beat.objects.all()
    for i in d:
        ran_beat[i.range_id]=i.range_id
    beat={}
    r=Range_beat.objects.all()
    for i in r:
        beat[i.range_id]=i.beat_id
    print(ran_beat,beat)
    return render(request,'allotbeat.html',{'ran_beat':ran_beat,'beat':beat})

def getexcelanimal(request):
    workbook = xlsxwriter.Workbook('main/static/data.xlsx')
    worksheet = workbook.add_worksheet()  
    row = 0
    column = 0
    a=Animal.objects.all()
    aid=[]
    atype=[]
    lat=[]
    lon=[]
    for i in a:
        item=i.animal_id
        worksheet.write(row, column, item)
        column+=1
        item=i.animal_info
        worksheet.write(row, column, item)
        column+=1
        for j in range(len(i.latitude)):
            item=i.latitude[j]
            worksheet.write(row, column, item)
            column+=1
            item=i.longitude[j]
            worksheet.write(row, column, item)
            column-=1
            row+=1
        column-=2
    # content = ["ankit", "rahul", "priya", "harshita", 
    #                     "sumit", "neeraj", "shivam"] 

    # for item in content : 
    #     worksheet.write(row, column, item) 
    #     row += 1
        
    workbook.close() 
    
    path = 'main/static/data.xlsx' # this should live elsewhere, definitely
    with open(path, "rb") as excel:
        data = excel.read()

    response = HttpResponse(data,content_type='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')
    response['Content-Disposition'] = 'attachment; filename=data.xlsx'
    return response
    # return render(request,"dashboard.html",{})

######BACK GROUND TASK########
@background(schedule=2)
def back():
    if not firebase_admin._apps:
        data = open('main/static/serviceAccount.json').read() #opens the json file and saves the raw contents
        jsonData = json.loads(data) #converts to a json structure

        cred = credentials.Certificate(jsonData)
        firebase_admin.initialize_app(cred)
    db = firestore.client()
    a=0
    print("in back new","ravi")
    # while(a<10):
    a+=1
    print(a)
    l=[]
    lon={}
    lat={}
    time={}
    c=Camera.objects.all()
    l=set(l)
    
    for i in c:
        l.add(str(i.camera_id))
        lon[str(i.camera_id)]=i.longitude
        lat[str(i.camera_id)]=i.latitude
    s=[]
    temp=Status.objects.all()
    s=set(s)
    now = datetime.datetime.now()
    for i in temp:
        # time[str(i.camera_id)]=i.time
        s.add(str(i.camera_id))
    ans=l-s
    if (len(ans)!=0):
        xyz=list(ans)
        print(time)
        for i in xyz:
            c = db.collection(u'camera').document('status')
            c.set({
                u'camera_id': i,
                u'latitude': lat[i],
                u'longitude': lon[i],
                u'time': now
            })

            pusher_client = pusher.Pusher(
            app_id='1038724',
            key='ed4d3bfd7a2e6650c539',
            secret='d87ae9e5262f74360a37',
            cluster='ap2',
            ssl=True
            )
            pusher_client.trigger('my-channel', 'my-event', {u'message': i+' camera is not working',u'lat':lat[i],u'lon':lon[i]})
    else:
        c = db.collection(u'camera').document('status')
        c.set({
            
        })
    
    d=Status.objects.all().delete()
    # time.sleep(30)


def fun():
    print('call')
    return

###############firestore##################
callback_done = threading.Event()
if not firebase_admin._apps:
    data = open('main/static/serviceAccount.json').read() #opens the json file and saves the raw contents
    jsonData = json.loads(data) #converts to a json structure

    cred = credentials.Certificate(jsonData)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
db = firestore.client()

def on_snapshot(doc_snapshot, changes, read_time):
    for doc in doc_snapshot:    
        dic=doc.to_dict()
        if dic:
            # print(dic)
            r=Report()
            r.empid=dic['empid']
            res = bytes(dic['image'], 'utf-8')
            r.image=res
            r.description=dic['description']
            r.latitude=dic['latitude']
            r.longitude=dic['longitude']
            r.save()
            print(dic['description'])
        # print(dic['image'])
    callback_done.set()

# Create a callback on_snapshot function to capture changes

doc_ref = db.collection(u'report').document(u'animal_report')

# Watch the document
doc_watch = doc_ref.on_snapshot(on_snapshot)


###TASK####
callback_done_task = threading.Event()
def on_snapshot_task(doc_snapshot, changes, read_time):
    tid=''
    for doc in doc_snapshot:    
        dic=doc.to_dict()
        if dic:
            # print(dic)
            t=Tasks.objects.filter(task_id=dic['task_id']).update(status='complete')
            t=Range_tasks.objects.filter(task_id=dic['task_id']).update(status='complete')
            t=Division_tasks.objects.filter(task_id=dic['task_id']).update(status='complete')
            rep=Task_Description()
            rep.task_id=dic['task_id']
            tid=dic['task_id']
            # rep.description=dic['description']
            res = bytes(dic['image'], 'utf-8') 
            rep.image=res
            rep.save()
            # print(dic)
        # print(dic['image'])
            e = db.collection(u'task').document(u'assign')
            di = e.get().to_dict()
            # print(di,dic['empid'])
            for i in range(len(di[dic['empid']])):
                print(i)
                if di[dic['empid']][i]['task_id']==tid:
                    print(di[dic['empid']][i])
                    del(di[dic['empid']][i])
                    break
            e.set(di)

    callback_done_task.set()

doc_ref_task = db.collection(u'task').document(u'complete')

# Watch the document
doc_watch_task = doc_ref_task.on_snapshot(on_snapshot_task)

###Local reports###
callback_done = threading.Event()
if not firebase_admin._apps:
    data = open('main/static/serviceAccount.json').read() #opens the json file and saves the raw contents
    jsonData = json.loads(data) #converts to a json structure

    cred = credentials.Certificate(jsonData)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
db = firestore.client()

def on_snapshot(doc_snapshot, changes, read_time):
    for doc in doc_snapshot:    
        dic=doc.to_dict()
        if dic:
            # print(dic)
            r=Local_report()
            # r.empid=dic['empid']
            res = bytes(dic['image'], 'utf-8')
            r.image=res
            r.description=dic['description']
            r.phone_no=dic['Phone']
            r.latitude=dic['latitude']
            r.longitude=dic['longitude']
            r.save()
            print(dic['description'])
        # print(dic['image'])
    callback_done.set()

# Create a callback on_snapshot function to capture changes

doc_ref = db.collection(u'report').document(u'local_report')

# Watch the document
doc_watch = doc_ref.on_snapshot(on_snapshot)




###############API#################
# class give_task(APIView):
#     def post(self,request,format=json):
#         print(request.data)
#         snippets = Tasks.objects.filter(task_to=request.data[0],status='assigned')
#         serializer = Task_serializer(snippets, many=True)
#         # return render(request,"appdata.html",{'data':serializer.data})
#         return Response(serializer.data)
#         # print(serializer.data)
#         # return Response({"data":[{"id":10},{"id":103}]})

# class manage_task(APIView):
#     # def get(self,request,format=None):
#     #     print(request.data)
#     #     snippets = Tasks.objects.filter(task_to=request.data)
#     #     serializer = Task_serializer(snippets, many=True)
#     #     # return render(request,"appdata.html",{'data':serializer.data})
#     #     # return Response(serializer.data)
#     #     return Response({'data':['a','b','asd']})

#     def post(self, request, format=None):
#         print(type(request.data))
#         if type(request.data) is list:
#             print(len(request.data))
#             for i in range(len(request.data)):
#                 serializer = Task_serializer(data=request.data[i])
#                 print("posting",serializer)
#                 if serializer.is_valid():
#                     print("valid",request.data[i]['task_id'])
#                     Tasks.objects.get(task_id=request.data[i]['task_id'],task_to=request.data[i]['task_to']).delete()
#                     Tasks.objects.filter(task_id=request.data[i]['task_id'],task_to=request.data[i]['task_from']).update(status='complete')
#                     serializer.save()

#             return Response([], status=status.HTTP_201_CREATED)
#         else:
#             serializer = Task_serializer(data=request.data)
#             print("posting",serializer)
#             if serializer.is_valid():
#                 print("valid",request.data['task_id'])
#                 Tasks.objects.get(task_id=request.data['task_id'],task_to=request.data['task_to']).delete()
#                 Tasks.objects.filter(task_id=request.data['task_id'],task_to=request.data['task_from']).update(status='complete')
#                 serializer.save()
                
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class manage_login(APIView):
    # def get(self,request,format=None):
    #     print(request.data)
    #     data={"id":10}
    #     print("get")
    #     return Response(data,status=status.HTTP_201_CREATED)

    def post(self,request,format=None):
        print(request.data)
        try:
            d=Forest_employee.objects.get(username=request.data['username'],password=request.data['password'])
            st=d.empid+'-'+d.name
            data={"id":st}
            print("post")
        except Forest_employee.DoesNotExist:
            data={"id":"-1"}
        return Response(data,status=status.HTTP_201_CREATED)

class alert(APIView):
    # count=set([])
    def post(self,request,format=None):
        # print(reque)
        now = datetime.datetime.now()
        if not firebase_admin._apps:
            data = open('main/static/serviceAccount.json').read() #opens the json file and saves the raw contents
            jsonData = json.loads(data) #converts to a json structure

            cred = credentials.Certificate(jsonData)
            firebase_admin.initialize_app(cred)
        db = firestore.client()

        if(request.data['type']=="local-alert"):
            print(request.data['type'])
            c = db.collection(u'camera').document('local-alert')
            c.set({
                u'latitude': request.data['latitude'],
                u'longitude': request.data['longitude'],
                u'time' : request.data['timestamp']
            })
            time.sleep(5)
            c.set({
                
            })
        else:
            if(request.data['type']=="working"):
                x=Status()
                x.latitude=float(request.data['latitude'])
                x.longitude=float(request.data['longitude'])
                x.camera_id=request.data['value']
                x.action=request.data['type']
                x.time=str(request.data['timestamp'])
                x.save()
            elif (request.data['type']=="hunter"):
                pusher_client = pusher.Pusher(
                    app_id='1038724',
                    key='ed4d3bfd7a2e6650c539',
                    secret='d87ae9e5262f74360a37',
                    cluster='ap2',
                    ssl=True
                    )
                pusher_client.trigger('my-channel', 'my-event', {u'message': u'Hunter detected',u'lat':u'12.485125',u'lon':u'92.889391'})
                x=Logs()
                x.latitude=float(request.data['latitude'])
                x.longitude=float(request.data['longitude'])
                x.camera_id=request.data['value']
                x.action=request.data['type']
                x.time=str(request.data['timestamp'])
                x.save()
                c = db.collection(u'camera').document('hunter')
                c.set({
                    u'camera_id': request.data['value'],
                    u'latitude': request.data['latitude'],
                    u'longitude': request.data['longitude'],
                    u'time' : request.data['timestamp']
                })
                time.sleep(5)
                c.set({
                    
                })
            elif request.data['type']=="sos":
                x=Logs()
                x.latitude=21.23
                x.longitude=73.75
                x.camera_id=request.data['value']
                x.action=request.data['type']
                x.time=str(request.data['timestamp'])
                x.save()
                pusher_client = pusher.Pusher(
                    app_id='1038724',
                    key='ed4d3bfd7a2e6650c539',
                    secret='d87ae9e5262f74360a37',
                    cluster='ap2',
                    ssl=True
                    )
                pusher_client.trigger('my-channel', 'my-event', {u'message': 'Sos is detected',u'lat':str(22.5),u'lon':str(73.5)})

            else:
                x=Logs()
                x.latitude=float(request.data['latitude'])
                x.longitude=float(request.data['longitude'])
                x.camera_id=request.data['value']
                x.action=request.data['type']
                x.time=str(request.data['timestamp'])
                x.save()
                pusher_client = pusher.Pusher(
                    app_id='1038724',
                    key='ed4d3bfd7a2e6650c539',
                    secret='d87ae9e5262f74360a37',
                    cluster='ap2',
                    ssl=True
                    )
                pusher_client.trigger('my-channel', 'my-event', {u'message': str(request.data['type'])+' is detected',u'lat':str(request.data['latitude']),u'lon':str(request.data['longitude'])})
            
        return Response(status=status.HTTP_201_CREATED)

class backtask(APIView):
    def post(self,request,format=None):
        back(repeat=5,repeat_until=datetime.datetime.now()+datetime.timedelta(0,600))
        process = subprocess.Popen(['python', 'manage.py','process_tasks'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        return Response(status=status.HTTP_201_CREATED)