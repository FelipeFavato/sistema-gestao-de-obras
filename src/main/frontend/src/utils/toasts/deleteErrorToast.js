import { toast} from 'vue3-toastify';

export function deleteErrorToast (texto) {
  toast.error('Não foi possível deletar, pois há ' + texto +  ' vinculados(as)!', {
    autoClose: 5000
  });
}