# Generated by Django 3.0.4 on 2020-08-03 09:02

import django.contrib.postgres.fields
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Animal',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('animal_id', models.CharField(max_length=200)),
                ('animal_name', models.CharField(max_length=200)),
                ('animal_info', models.CharField(max_length=500)),
                ('latitude', django.contrib.postgres.fields.ArrayField(base_field=models.FloatField(), size=None)),
                ('longitude', django.contrib.postgres.fields.ArrayField(base_field=models.FloatField(), size=None)),
            ],
        ),
        migrations.CreateModel(
            name='Beat_employee',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('beat_id', models.CharField(max_length=200)),
                ('empid', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Camera',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('camera_id', models.CharField(max_length=200)),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
                ('status', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Division_range',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('division_id', models.CharField(max_length=200)),
                ('range_id', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Division_tasks',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('task_id', models.CharField(max_length=200)),
                ('task_info', models.CharField(max_length=200)),
                ('deadline', models.DateField()),
                ('task_from', models.CharField(max_length=200)),
                ('task_to', models.CharField(max_length=200)),
                ('status', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Forest_employee',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('area', models.CharField(max_length=200)),
                ('name', models.CharField(max_length=200)),
                ('empid', models.CharField(max_length=200)),
                ('role', models.CharField(max_length=200)),
                ('username', models.CharField(max_length=200)),
                ('password', models.CharField(max_length=200)),
                ('latitude', django.contrib.postgres.fields.ArrayField(base_field=models.FloatField(), size=None)),
                ('longitude', django.contrib.postgres.fields.ArrayField(base_field=models.FloatField(), size=None)),
            ],
        ),
        migrations.CreateModel(
            name='Local_report',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('description', models.CharField(max_length=1000)),
                ('phone_no', models.CharField(max_length=10)),
                ('image', models.BinaryField()),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
            ],
        ),
        migrations.CreateModel(
            name='Login',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('username', models.CharField(max_length=200)),
                ('password', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Logs',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('camera_id', models.CharField(max_length=200)),
                ('time', models.CharField(max_length=200)),
                ('action', models.CharField(max_length=200)),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
            ],
        ),
        migrations.CreateModel(
            name='Range_beat',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('range_id', models.CharField(max_length=200)),
                ('beat_id', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Range_tasks',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('task_id', models.CharField(max_length=200)),
                ('task_info', models.CharField(max_length=200)),
                ('deadline', models.DateField()),
                ('task_from', models.CharField(max_length=200)),
                ('task_to', models.CharField(max_length=200)),
                ('status', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Report',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('empid', models.CharField(max_length=200)),
                ('description', models.CharField(max_length=1000)),
                ('image', models.BinaryField()),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
            ],
        ),
        migrations.CreateModel(
            name='Researcher',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('researcher_id', models.CharField(max_length=200)),
                ('researcher_name', models.CharField(max_length=200)),
                ('animal', django.contrib.postgres.fields.ArrayField(base_field=models.CharField(max_length=200), size=None)),
                ('experience', models.CharField(max_length=200)),
                ('qualification', models.CharField(max_length=200)),
                ('username', models.CharField(max_length=200)),
                ('password', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Status',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('camera_id', models.CharField(max_length=200)),
                ('time', models.CharField(max_length=200)),
                ('action', models.CharField(max_length=200)),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
            ],
        ),
        migrations.CreateModel(
            name='Task_Description',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('task_id', models.CharField(max_length=200)),
                ('description', models.CharField(max_length=1000)),
                ('image', models.BinaryField()),
            ],
        ),
        migrations.CreateModel(
            name='Tasks',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('task_id', models.CharField(max_length=200)),
                ('task_info', models.CharField(max_length=200)),
                ('deadline', models.DateField()),
                ('task_from', models.CharField(max_length=200)),
                ('task_to', models.CharField(max_length=200)),
                ('status', models.CharField(max_length=200)),
            ],
        ),
    ]
