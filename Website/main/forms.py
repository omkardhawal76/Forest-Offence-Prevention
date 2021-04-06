from .models import *
from django import forms


class addanimalform(forms.ModelForm):
	class Meta:
		model = Animal
		exclude = ()

class addresearcherform(forms.ModelForm):
	class Meta:
		model = Researcher
		exclude = ()

class addcameraform(forms.ModelForm):
	class Meta:
		model = Camera
		exclude = ()

class addlogsform(forms.ModelForm):
	class Meta:
		model = Logs
		exclude = ()

class adduserform(forms.ModelForm):
	class Meta:
		model = Login
		exclude = ()