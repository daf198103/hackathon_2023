<h1>TestCreator<h1>

We create an api that integrates with chatGPT and asks through a prompt a creation of a code challenge
sending: 
 - language 
 - version
 - idiom 

we want it to be created. After that we would apply the
code challenge on the candidate. After he finishes we send his solution through another endpoint asking
chatGPT to evaluate the proposed solution.


- apikeys -

Para gerar as apikeys caso tenha expirado e de erro 401 , acesse a url.
https://platform.openai.com/ 

Acesse com a conta google 
email : hackaton31@gmail.com
senha Q1W2E3R4T5Y6

vá em apikeys, crie uma apikey nova e anote
troque em app.api_key no arquivo application.properties

Para Testar através do Postman seguem dois curls

Gerar:
curl --location --request POST 'http://localhost:8080/api/v1/create/chatgpt?language=Java&version=17&seniority=Mid&idiom=' \
--data ''

Avaliar:
curl --location 'http://localhost:8080/api/v1/evaluate?apiKey=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' \
--header 'Content-Type: application/json' \
--data '{
"public static int binarySearch(int[] array, int target) {
int left = 0;
int right = array.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        // Check if target is present at mid
        if (array[mid] == target) {
            return mid;
        }

        // If target is greater, ignore the left half
        if (array[mid] < target) {
            left = mid + 1;
        }

        // If target is smaller, ignore the right half
        else {
            right = mid - 1; // Corrected from right = mid + 1;
        }
    }

    // If the target is not present in the array
    return -1;
    }"
}'