#include <iostream>
#include <cstdlib>
#include <cstdio>
#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>

using namespace std;

pthread_mutex_t output_lock;

const int MAX = 3;
int buffer = 0; //shared resource
int *times = new int[MAX];

void * addone( void * );

int main(int argc, char *argv[])
{
	int num_threads = 3;
	pthread_t *thread_ids;
	void  *p_status;
//initialize dynamic array times
	for (int i = 0; i < MAX; i++)
     	{
		times[i] = 0;
	}
//Set up an output lock so that threads wait their turn to speak.
	if (pthread_mutex_init(&output_lock, NULL)!=0)
	{
		perror("Could not create mutex for output: ");
		return 1;
	}
	thread_ids = new pthread_t[MAX];
// generate threads 
	for (int i = 0; i < MAX; i++)
	{
		int *arg = new int;
		*arg = i;
		if( pthread_create(&thread_ids[i],NULL,addone,arg) > 0)
		{
			perror("creating thread:");
		        return 2;
		}
	}
// join threads and print their return values
	for (int i = 0; i < MAX; i++)
	{
		if (pthread_join(thread_ids[i], &p_status) != 0)
		{
			perror("trouble joining thread: ");
			return 3;
		}
//Threads may still be building their return, so lock stdout 
    	        if (pthread_mutex_lock(&output_lock) != 0)
		{
			perror("Could not lock output: ");
			return 4;
		}
		cout << "Thread " << i << ": " << (char *)p_status << endl;
		if (pthread_mutex_unlock(&output_lock) != 0)
		{
			perror("Could not unlock output: ");
		    	return 5;
		}
		
		delete [] (char *)p_status;
	}
	
	
	cout << "Total access is " << times[0] + times[1] + times[2];
	delete [] times;
	return 0;

}


void * addone(void *num)
{
	int t_num = *(int *)num;
	char *msg = new char[255];
	while (buffer < 24)
	{
		if (pthread_mutex_lock(&output_lock) != 0)
		{
			perror("Could not lock output: ");
			exit(4); //something horrible happened - exit whole program with error
		}
		cout << "Thread ID: " << pthread_self() <<" PID: " << getpid() << " buffer " << buffer << endl;
		buffer++;
		times[t_num]++;
		
		if (pthread_mutex_unlock(&output_lock) != 0)
		{
			perror("Could not unlock output: ");
			exit(5); //something horrible happened - exit whole program with error
		}
		if (times[t_num] > MAX/3)
			sleep(0.5);
	}
	snprintf(msg, 255, "The thread id %ld worked on buffer %d times", pthread_self(), times[t_num]);
	return msg;
}
