################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../AudioWrap.cpp \
../VoiceFilterSpark.cpp 

OBJS += \
./AudioWrap.o \
./VoiceFilterSpark.o 

CPP_DEPS += \
./AudioWrap.d \
./VoiceFilterSpark.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"/home/abel/dev/workspace/include" -I"/home/abel/dev/workspace/Include" -I"/home/abel/dev/workspace/Dependencies/ffmpeg-0.7.12" -O3 -g3 -Wall -c -fmessage-length=0 -std=c++0x -D__STDC_CONSTANT_MACROS -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


