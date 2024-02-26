import { toast} from 'vue3-toastify';

export function unAssignSuccessToast () {
  toast.success('Sócio(a) DESASSOCIADO(A) com sucesso!', {
    autoClose: 3000
  });
}